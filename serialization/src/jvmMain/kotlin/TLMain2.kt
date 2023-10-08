package kotl.serialization

import kotl.serialization.annotation.Crc32
import kotl.serialization.annotation.TLSize
import kotl.serialization.extensions.asTLDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

@Serializable
@Crc32(value = 0x05162463_u)
private class TLResponsePQ(
    @TLSize(bits = 128)
    val nonce: IntArray,
    @TLSize(bits = 128)
    val serverNonce: IntArray,
    val pq: String,
    val serverPublicKey: List<Long>
)

private fun main() {
    println(serializer<TLResponsePQ>().descriptor.asTLDescriptor())
}
