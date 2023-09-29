package kotl.core.decoder

import kotl.core.descriptor.TLStringDescriptor.STRING_SIZE_MAGIC_NUMBER
import kotl.stdlib.bytes.decodeInt
import kotl.stdlib.bytes.decodeLong

internal class ByteArrayInput(private val bytes: ByteArray) {
    private var position: Int = 0

    fun scanInt(): Int = bytes.decodeInt(position)

    fun readInt(): Int {
        val int = scanInt()
        position += Int.SIZE_BYTES
        return int
    }

    fun readLong(): Long {
        val long = bytes.decodeLong(position)
        position += Long.SIZE_BYTES
        return long
    }

    fun readDouble(): Double {
        val double = Double.fromBits(readLong())
        return double
    }

    fun readString(): String {
        val head = bytes[position]
        position++
        return if (head.toUByte() == STRING_SIZE_MAGIC_NUMBER) {
            readLongString()
        } else {
            readShortString(head)
        }
    }

    private fun readLongString(): String {
        val sizeBytes = bytes.sliceArray(position..(position + 2)) + 0
        val size = sizeBytes.decodeInt(offset = 0)
        position += (Int.SIZE_BYTES - 1) // one was already read for head
        val bytes = ByteArray(size) { i -> bytes[position + i] }
        position += size
        return bytes.decodeToString()
    }

    private fun readShortString(head: Byte): String {
        val size = head.toInt() and 0xff
        val bytes = ByteArray(size) { i -> bytes[position + i] }
        position += size
        return bytes.decodeToString()
    }
}
