package kotl.serialization.decoder

import kotl.core.element.*
import kotl.serialization.TL
import kotl.serialization.annotation.TLSize
import kotl.serialization.extensions.crc32
import kotl.serialization.extensions.tlRpc
import kotl.serialization.extensions.tlSize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalSerializationApi::class)
internal class TLDecoder(
    private val tl: TL,
    private val reader: TLElementReader,
    private val parentDescriptor: SerialDescriptor? = null
) : AbstractDecoder() {
    private var lastElementIndex = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (reader.isDone()) return CompositeDecoder.DECODE_DONE
        return lastElementIndex++
    }

    override fun decodeBoolean(): Boolean {
        val element = reader.nextTLElement()
        element as? TLBoolean
            ?: throw SerializationException("TLBoolean expected, but $element found")
        return element.boolean
    }

    override fun decodeInt(): Int {
        val element = reader.nextTLElement()
        element as? TLInt32
            ?: throw SerializationException("TLInt32 expected, but $element found")
        return element.int
    }

    override fun decodeLong(): Long {
        val element = reader.nextTLElement()
        element as? TLInt64
            ?: throw SerializationException("TLInt64 expected, but $element found")
        return element.long
    }

    override fun decodeDouble(): Double {
        val element = reader.nextTLElement()
        element as? TLDouble
            ?: throw SerializationException("TLDouble expected, but $element found")
        return element.double
    }

    override fun decodeString(): String {
        val element = reader.nextTLElement()
        element as? TLString
            ?: throw SerializationException("TLString expected, but $element found")
        return element.string
    }

    override fun decodeNull(): Nothing? {
        val element = reader.nextTLElement()
        element as? TLNull
            ?: throw SerializationException("TLNull expected, but $element found")
        return null
    }

    override fun beginStructure(
        descriptor: SerialDescriptor
    ): CompositeDecoder {
        if (descriptor.kind == PolymorphicKind.SEALED) {
            val constructor = reader.nextTLElement()
            constructor as? TLConstructor ?: throw SerializationException("TLConstructor expected, but $constructor got")

            val crc32 = constructor.crc32
            val constructorDescriptor = descriptor.getElementDescriptor(index = 1).elementDescriptors
                .firstOrNull { child -> child.crc32?.value == crc32 }

            val className = constructorDescriptor?.serialName

            if (className == null) {
                val constructors = descriptor.getElementDescriptor(index = 1)
                    .elementDescriptors
                    .mapNotNull { child -> child.crc32?.value }
                error("Cannot find constructor with id ${crc32.toString(radix = 16)}. Available constructors: ${constructors.map { it.toString(radix = 16) }}")
            }

            return TLDecoder(tl, SealedElementReader(className, constructor))
        }

        return when (descriptor.kind as? StructureKind) {
            StructureKind.LIST -> {
                val vector = reader.nextTLElement()
                val intDecoder = intDecoder(vector)
                if (intDecoder != null) return intDecoder
                vector as? TLVector ?: throw SerializationException("TLVector expected, but $vector got")
                TLDecoder(tl, ListElementReader(vector))
            }
            StructureKind.CLASS, StructureKind.OBJECT -> {
                val crc32 = descriptor.crc32
                val rpcCall = descriptor.tlRpc

                val constructor = reader.nextTLElement()
                constructor as? TLConstructor ?: throw SerializationException("TLConstructor expected, but $constructor got")

                when {
                    crc32 != null && rpcCall != null -> throw SerializationException("You should not annotate class with both @Crc32 or @TLRpcCall, use @Crc32 for constructors and @TLRpcCall for functions")
                    crc32 != null -> TLDecoder(tl, ConstructorElementReader(constructor), descriptor)
                    rpcCall != null -> throw SerializationException("@TLRpcCall does not intended to be deserialized")
                    else -> throw SerializationException("Your class should be annotated with @Crc32 for constructors or @TLRpcCall for functions to make it compatible with TL")
                }
            }
            else -> error("Unsupported structure kind ${descriptor.kind}")
        }
    }

    private fun intDecoder(int: TLElement): TLDecoder? {
        if (int !is TLInt) return null

        val index = lastElementIndex - 1
        val size = parentDescriptor
            ?.getElementAnnotations(index)
            ?.filterIsInstance<TLSize>()
            ?.firstOrNull()

        require(size != null && size.bits == (int.data.size * Int.SIZE_BITS)) {
            "descriptor.size: ${size?.bits}, vector.size: ${int.data.size}"
        }
        return TLDecoder(tl, IntElementReader(int))
    }

    override fun decodeByte(): Byte = unsupportedPrimitive("Byte")
    override fun decodeChar(): Char = unsupportedPrimitive("Char")
    override fun decodeFloat(): Float = unsupportedPrimitive("Float")
    override fun decodeShort(): Short = unsupportedPrimitive("Short")
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = error("TL doesn't support enums")

    override val serializersModule: SerializersModule
        get() = tl.serializersModule

    private fun unsupportedPrimitive(
        name: String
    ): Nothing = throw SerializationException("TL doesn't support $name. All supported primitives are: Boolean, Int, Long, String, Double")
}
