package kotl.serialization

import kotl.core.element.TLElement
import kotl.core.encoder.encodeToByteArray
import kotl.serialization.encode.SingleElementWriter
import kotl.serialization.encode.TLEncoder
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
    override fun <T> decodeFromByteArray(
        deserializer: DeserializationStrategy<T>,
        bytes: ByteArray
    ): T {
        TODO("Not yet implemented")
    }

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
