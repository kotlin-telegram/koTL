package kotl.serialization.int

import kotl.serialization.annotation.TLSize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

@Serializable(with = Int128.Serializer::class)
public data class Int128(
    @TLSize(bits = 128)
    public val data: IntArray
) {
    init {
        require(data.size == 4) { "int128 expected, but int${data.size * Int.SIZE_BITS} got" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Int128) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    public object Serializer : KSerializer<Int128> {

        @OptIn(ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor = SerialDescriptor(
            serialName = "kotl.serialization.int.Int128",
            original = Int128Serializable.serializer().descriptor
        )

        override fun deserialize(decoder: Decoder): Int128 {
            val serializable = decoder.decodeSerializableValue(
                deserializer = Int128Serializable.serializer()
            )
            return Int128(serializable.data)
        }

        override fun serialize(encoder: Encoder, value: Int128) {
            val serializable = Int128Serializable(value.data)
            encoder.encodeSerializableValue(
                serializer = Int128Serializable.serializer(),
                value = serializable
            )
        }
    }
}

@Serializable
@JvmInline
private value class Int128Serializable(
    @TLSize(bits = 128)
    val data: IntArray
)
