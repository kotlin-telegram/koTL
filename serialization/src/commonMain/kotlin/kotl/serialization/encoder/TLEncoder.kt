package kotl.serialization.encoder

import kotl.core.element.typedLanguage
import kotl.serialization.TL
import kotl.serialization.extensions.tlFunction
import kotl.serialization.extensions.tlType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder

@OptIn(ExperimentalSerializationApi::class)
internal class TLEncoder(
    private val tl: TL,
    private val writer: TLElementWriter
): AbstractEncoder() {
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

    override fun encodeInt(value: Int) = writer.writeTLElement(value.typedLanguage)
    override fun encodeLong(value: Long) = writer.writeTLElement(value.typedLanguage)
    override fun encodeDouble(value: Double) = writer.writeTLElement(value.typedLanguage)
    override fun encodeString(value: String) = writer.writeTLElement(value.typedLanguage)
    override fun encodeBoolean(value: Boolean) = writer.writeTLElement(value.typedLanguage)
    override fun encodeNull() = writer.writeTLElement(null.typedLanguage)

    override fun beginCollection(
        descriptor: SerialDescriptor,
        collectionSize: Int
    ): CompositeEncoder = when (descriptor.kind as StructureKind) {
        StructureKind.LIST -> TLEncoder(tl, ListElementWriter(writer))
        StructureKind.MAP -> throw SerializationException("TL doesn't support maps")
        else -> error("Unknown collection kind ${descriptor.kind}")
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        if (descriptor.kind == PolymorphicKind.SEALED) {
            descriptor.tlType ?: throw SerializationException("Sealed Type should be annotated with @TLType to make it compatible with TLFormat")
            return this
        }

        when (descriptor.kind as StructureKind) {
            StructureKind.CLASS, StructureKind.OBJECT -> {
                val tlFunction = descriptor.tlFunction
                    ?: throw SerializationException("Class should be annotated with @TLFunction to make it compatible with TL format")
                return TLEncoder(tl, FunctionElementWriter(tlFunction.crc32, writer))
            }
            else -> error("Unknown structure kind ${descriptor.kind}")
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        if (descriptor.kind == PolymorphicKind.SEALED) return
        writer.endStructure()
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        if (descriptor.kind != PolymorphicKind.SEALED) return true
        return index > 0
    }

    override fun encodeByte(value: Byte) = unsupportedPrimitive("Byte")
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
