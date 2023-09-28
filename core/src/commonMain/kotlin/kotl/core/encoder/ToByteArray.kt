package kotl.core.encoder

import kotl.core.element.*
import kotl.stdlib.bytes.encodeToByteArray
import kotl.stdlib.bytes.padEnd
import kotl.stdlib.int.nearestMultipleOf

public fun TLElement.encodeToByteArray(): ByteArray = when (this) {
    is TLFunction -> encodeToByteArray()
    is TLDouble -> double.toBits().encodeToByteArray()
    is TLInt -> int.encodeToByteArray()
    is TLLong -> long.encodeToByteArray()
    is TLString -> string.encodeToByteArrayTL()
    is TLVector -> asFunction().encodeToByteArray()
    is TLTrue -> asFunction().encodeToByteArray()
    is TLFalse -> asFunction().encodeToByteArray()
    is TLNull -> asFunction().encodeToByteArray()
}

private fun TLFunction.encodeToByteArray(): ByteArray {
    val header = crc32.toInt().encodeToByteArray()
    val parameters = parameters.fold(byteArrayOf()) { data, element ->
        data + element.encodeToByteArray()
    }
    return header + parameters
}

private fun TLVector.asFunction() = TLFunction(
    crc32 = TLVector.CRC32,
    parameters = listOf(elements.size.typedLanguage) + elements
)

private fun TLTrue.asFunction() = TLFunction(
    crc32 = CRC32,
    parameters = emptyList()
)

private fun TLFalse.asFunction() = TLFunction(
    crc32 = CRC32,
    parameters = emptyList()
)

private fun TLNull.asFunction() = TLFunction(
    crc32 = CRC32,
    parameters = emptyList()
)

/**
 * The values of type string look differently depending on the length L of the string being serialized:
 *
 * If L <= 253, the serialization contains one byte with the value of L,
 * then L bytes of the string followed by 0 to 3 characters containing 0,
 * such that the overall length of the value be divisible by 4,
 * whereupon all of this is interpreted as a sequence of
 * int(L/4)+1 32-bit numbers.
 *
 * If L >= 254, the serialization contains byte 254,
 * followed by 3 bytes with the string length L, followed by
 * L bytes of the string, further followed by 0 to 3 null padding bytes.
 *
 * Source: https://core.telegram.org/mtproto/serialize
 */
private const val STRING_SIZE_MAGIC_NUMBER: UByte = 254_u

private fun String.encodeToByteArrayTL(): ByteArray =
    if (length < STRING_SIZE_MAGIC_NUMBER.toInt()) {
        encodeSmallString()
    } else {
        encodeBigString()
    }

private fun String.encodeSmallString(): ByteArray {
    val unpadded = byteArrayOf(length.toByte()) + this.encodeToByteArray()
    val desiredLength = unpadded.size.nearestMultipleOf(n = 4)
    return unpadded.padEnd(desiredLength)
}

// todo: untested
private fun String.encodeBigString(): ByteArray {
    val unpadded = byteArrayOf(STRING_SIZE_MAGIC_NUMBER.toByte()) +
            length.encodeToByteArray().dropLast(n = 1) +
            this.encodeToByteArray()

    val desiredLength = unpadded.size.nearestMultipleOf(n = 4)

    return unpadded.padEnd(desiredLength)
}
