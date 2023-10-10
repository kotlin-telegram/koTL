package kotl.serialization.bytes

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = Bytes.Serializer::class)
public data class Bytes(public val payload: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bytes) return false
        return payload.contentEquals(other.payload)
    }

    override fun hashCode(): Int {
        return payload.contentHashCode()
    }

    @OptIn(ExperimentalSerializationApi::class)
    public object Serializer : KSerializer<Bytes> {
        override val descriptor: SerialDescriptor = SerialDescriptor(
            serialName = "kotl.serialization.bytes.Bytes",
            original = ByteArraySerializer().descriptor
        )

        override fun deserialize(decoder: Decoder): Bytes {
            val payload = decoder.decodeSerializableValue(ByteArraySerializer())
            return Bytes(payload)
        }

        override fun serialize(encoder: Encoder, value: Bytes) {
            encoder.encodeSerializableValue(ByteArraySerializer(), value.payload)
        }
    }
}
