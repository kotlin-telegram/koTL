package kotl.stdlib.bytes

public fun Int.encodeToByteArray(): ByteArray = byteArrayOf(
    (this shr  0).toByte(),
    (this shr  8).toByte(),
    (this shr 16).toByte(),
    (this shr 24).toByte()
)
