package kotl.core.encoder

import kotl.core.descriptor.TLStringDescriptor.STRING_SIZE_MAGIC_NUMBER
import kotl.core.element.*
import kotl.stdlib.bytes.encodeToByteArray
import kotl.stdlib.bytes.padEnd
import kotl.stdlib.int.nearestMultipleOf

public fun TLElement.encodeToByteArray(): ByteArray = when (this) {
    is TLConstructor -> encodeToByteArray()
    is TLFunction -> asConstructor().encodeToByteArray()
    is TLInt -> encodeToByteArray()
    is TLDouble -> double.toBits().encodeToByteArray()
    is TLString -> string.encodeStringTL()
    is TLBytes -> bytes.encodeBytesTL()
    is TLVector -> asTLConstructor().encodeToByteArray()
    is TLTrue -> asTLConstructor().encodeToByteArray()
    is TLFalse -> asTLConstructor().encodeToByteArray()
    is TLNull -> asTLConstructor().encodeToByteArray()
}

private fun TLConstructor.encodeToByteArray(): ByteArray {
    val header = crc32.toInt().encodeToByteArray()
    val parameters = parameters.fold(byteArrayOf()) { data, element ->
        data + element.encodeToByteArray()
    }
    return header + parameters
}

// fixme: pretty slow and memory-consuming
private fun TLInt.encodeToByteArray(): ByteArray {
    return data.fold(byteArrayOf()) { acc, int ->
        acc + int.encodeToByteArray()
    }
}

private fun TLFunction.asConstructor(): TLConstructor = TLConstructor(crc32, parameters)

private fun String.encodeStringTL(): ByteArray =
    encodeToByteArray().encodeBytesTL()

private fun ByteArray.encodeBytesTL(): ByteArray =
    if (size < STRING_SIZE_MAGIC_NUMBER.toInt()) {
        encodeSmallString()
    } else {
        encodeBigString()
    }

private fun ByteArray.encodeSmallString(): ByteArray {
    val unpadded = byteArrayOf(size.toByte()) + this
    val desiredLength = unpadded.size.nearestMultipleOf(n = 4)
    return unpadded.padEnd(desiredLength)
}

private fun ByteArray.encodeBigString(): ByteArray {
    val unpadded = byteArrayOf(STRING_SIZE_MAGIC_NUMBER.toByte()) +
            size.encodeToByteArray().dropLast(n = 1) +
            this

    val desiredLength = unpadded.size.nearestMultipleOf(n = 4)

    return unpadded.padEnd(desiredLength)
}
