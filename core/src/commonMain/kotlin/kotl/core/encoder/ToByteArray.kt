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
    is TLString -> string.encodeToByteArrayTL()
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
