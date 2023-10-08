package kotl.serialization

import kotl.serialization.annotation.Crc32
import kotl.serialization.annotation.TLSize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

@Serializable
@Crc32(value = 0xf0f0f0f_u)
private data class ExchangePQ2(
    @TLSize(bits = 128)
    val nonce: IntArray
)

private fun main() {
    val data = ExchangePQ2(
        nonce = intArrayOf(0, 0, 0, 0)
    )
    println("DATA: $data")
    val bytes = TL.encodeToByteArray(data)
    println("BYTES: ${bytes.joinToString(separator = " ") { it.toUByte().toString(16).padStart(2, '0') }}")
    val decoded = TL.decodeFromByteArray<ExchangePQ2>(bytes)
    println("DECODED: $decoded")
}
