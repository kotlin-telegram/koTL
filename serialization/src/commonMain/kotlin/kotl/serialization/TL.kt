package kotl.serialization

import kotl.core.decoder.decodeFromByteArray
import kotl.core.descriptor.TLExpressionDescriptor
import kotl.core.element.TLElement
import kotl.core.element.TLExpression
import kotl.core.encoder.encodeToByteArray
import kotl.serialization.decoder.SingleElementReader
import kotl.serialization.decoder.TLDecoder
import kotl.serialization.encoder.SingleElementWriter
import kotl.serialization.encoder.TLEncoder
import kotl.serialization.extensions.asTLDescriptor
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

/**
 * @param supportOptionalHash whether to enable hash calculation
 *  when no hash present or fail
 */
public sealed class TL(
    public val supportOptionalHash: Boolean,
    override val serializersModule: SerializersModule
) : BinaryFormat {

    public fun <T> decodeFromTLElement(
        deserializer: DeserializationStrategy<T>,
        element: TLElement
    ): T {
        val reader = SingleElementReader(element)
        val decoder = TLDecoder(this, reader)
        return decoder.decodeSerializableValue(deserializer)
    }

    override fun <T> decodeFromByteArray(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray
    ): T = decodeFromTLElement(
        deserializer = deserializer,
        element = parseToTLElement(
            descriptor = deserializer.descriptor.asTLDescriptor(),
            bytes = bytes
        )
    )

    public fun parseToTLElement(
        descriptor: TLExpressionDescriptor,
        bytes: ByteArray
    ): TLExpression = descriptor.decodeFromByteArray(bytes)

    public fun <T> encodeToTLElement(
        serializer: SerializationStrategy<T>,
        value: T
    ): TLElement {
        val writer = SingleElementWriter()
        val encoder = TLEncoder(this, writer)
        encoder.encodeSerializableValue(serializer, value)
        return writer.encoded
    }

    override fun <T> encodeToByteArray(
        serializer: SerializationStrategy<T>,
        value: T
    ): ByteArray = encodeToTLElement(serializer, value)
        .encodeToByteArray()

    public companion object Default : TL(
        supportOptionalHash = true,
        EmptySerializersModule()
    )
}

public inline fun <reified T> TL.encodeToTLElement(value: T): TLElement =
    encodeToTLElement(serializer<T>(), value)

public inline fun <reified T> TL.decodeFromTLElement(element: TLElement): T =
    decodeFromTLElement(serializer<T>(), element)
