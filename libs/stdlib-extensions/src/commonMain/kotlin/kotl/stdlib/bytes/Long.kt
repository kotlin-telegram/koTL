package kotl.stdlib.bytes

public fun Long.encodeToByteArray(): ByteArray = byteArrayOf(
    (this shr  0).toByte(),
    (this shr  8).toByte(),
    (this shr 16).toByte(),
    (this shr 24).toByte(),
    (this shr 32).toByte(),
    (this shr 40).toByte(),
    (this shr 48).toByte(),
    (this shr 56).toByte()
)
