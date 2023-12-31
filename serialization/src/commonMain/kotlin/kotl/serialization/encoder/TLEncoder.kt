package kotl.serialization.encoder

import kotl.core.descriptor.TLIntDescriptor
import kotl.core.element.TLExpression
import kotl.core.element.TLInt32
import kotl.core.element.typedLanguage
import kotl.serialization.TL
import kotl.serialization.annotation.TLBare
import kotl.serialization.annotation.TLSize
import kotl.serialization.extensions.crc32
import kotl.serialization.extensions.tlRpc
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
internal class TLEncoder(
    private val tl: TL,
    private val writer: TLElementWriter,
    private val parentDescriptor: SerialDescriptor? = null
): AbstractEncoder() {
    private var elementIndex: Int = 0

    override fun <T> encodeSerializableValue(
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        checkDescriptor(serializer.descriptor)
        super.encodeSerializableValue(serializer, value)
    }

    private val supportedDescriptors = listOf(
        PrimitiveKind.INT,
        PrimitiveKind.LONG,
        PrimitiveKind.DOUBLE,
        PrimitiveKind.STRING,
        StructureKind.CLASS,
        StructureKind.OBJECT,
        StructureKind.LIST,
        PolymorphicKind.SEALED
    )

    private fun checkDescriptor(descriptor: SerialDescriptor) {
        require(descriptor.kind in supportedDescriptors) { "TL doesn't support ${descriptor.kind}" }
    }

    override fun encodeInt(value: Int) {
        if (writer is IntElementWriter) {
            writer.writeElement(value)
        } else {
            writer.writeElement(TLInt32(value))
        }
    }

    override fun encodeByte(value: Byte) {
        if (writer is BytesElementWriter) {
            writer.writeElement(value)
        } else {
            unsupportedPrimitive("Byte")
        }
    }
    override fun encodeLong(value: Long) = writer.writeElement(value.typedLanguage)
    override fun encodeDouble(value: Double) = writer.writeElement(value.typedLanguage)
    override fun encodeString(value: String) = writer.writeElement(value.typedLanguage)
    override fun encodeBoolean(value: Boolean) = writer.writeElement(value.typedLanguage)
    override fun encodeNull() = writer.writeElement(null.typedLanguage)
    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        return TLEncoder(tl, writer, parentDescriptor = descriptor)
    }

    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): CompositeEncoder {
        return when (descriptor.kind as StructureKind) {
            StructureKind.LIST -> intEncoder(descriptor, collectionSize)
                ?: bytesEncoder(descriptor)
                ?: TLEncoder(tl, ListElementWriter(writer))
            StructureKind.MAP -> throw SerializationException("TL doesn't support maps")
            else -> error("Unknown collection kind ${descriptor.kind}")
        }
    }

    private fun intEncoder(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): TLEncoder? {
        val underlying = descriptor.getElementDescriptor(index = 0)
        if (underlying.kind != PrimitiveKind.INT) return null

        val sizeAnnotation = parentDescriptor
            ?.getElementAnnotations(elementIndex)
            ?.filterIsInstance<TLSize>()
            ?.firstOrNull()
        val size = sizeAnnotation?.bits ?: return null

        require(collectionSize * Int.SIZE_BITS == size) {
            "int$size expected, but int${collectionSize * Int.SIZE_BITS} got"
        }

        val writer = IntElementWriter(writer, TLIntDescriptor.of(size))
        return TLEncoder(tl, writer)
    }

    private fun bytesEncoder(
        descriptor: SerialDescriptor
    ): TLEncoder? {
        val underlying = descriptor.getElementDescriptor(index = 0)
        if (underlying.kind != PrimitiveKind.BYTE) return null
        val writer = BytesElementWriter(writer)
        return TLEncoder(tl, writer)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        if (descriptor.kind == PolymorphicKind.SEALED) return this

        when (descriptor.kind as? StructureKind) {
            StructureKind.CLASS, StructureKind.OBJECT -> {
                val crc32 = descriptor.crc32
                val rpcCall = descriptor.tlRpc

                val bare = parentDescriptor
                    ?.getElementAnnotations(elementIndex)
                    ?.filterIsInstance<TLBare>()
                    ?.firstOrNull()

                return when {
                    crc32 != null && rpcCall != null -> throw SerializationException("You should not annotate class with both @Crc32 or @TLRpcCall, use @Crc32 for constructors and @TLRpcCall for functions")
                    bare != null -> TLEncoder(tl, ConstructorElementWriter(crc32 = null, writer), descriptor)
                    crc32 != null -> TLEncoder(tl, ConstructorElementWriter(crc32.value, writer), descriptor)
                    rpcCall != null -> TLEncoder(tl, FunctionElementWriter(rpcCall.crc32, writer), descriptor)
                    else -> throw SerializationException("Your class ${descriptor.serialName} should be annotated with @Crc32 for constructors or @TLRpcCall for functions to make it compatible with TL")
                }
            }
            else -> error("Unsupported structure kind ${descriptor.kind}")
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (descriptor.kind == PolymorphicKind.SEALED) return
        writer.endStructure()
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        elementIndex = index
        if (descriptor.kind != PolymorphicKind.SEALED) return true
        return index > 0
    }

    override fun encodeChar(value: Char) = unsupportedPrimitive("Char")
    override fun encodeFloat(value: Float) = unsupportedPrimitive("Float")
    override fun encodeShort(value: Short) = unsupportedPrimitive("Short")
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) =
        throw SerializationException("TL doesn't support enums")

    private fun unsupportedPrimitive(
        name: String
    ): Nothing = throw SerializationException("TL doesn't support $name. All supported primitives are: Boolean, Int, Long, String, Double")

    override val serializersModule get() = tl.serializersModule
}
