package kotl.core.decoder

import kotl.core.descriptor.*
import kotl.core.element.*

public fun TLExpressionDescriptor.decodeFromByteArray(byteArray: ByteArray): TLExpression =
    decodeFromBytes(ByteArrayInput(byteArray))

public fun TLTypeDescriptor.decodeFromByteArray(byteArray: ByteArray): TLConstructor =
    decodeFromBytes(ByteArrayInput(byteArray))

public fun TLVectorDescriptor.decodeFromByteArray(byteArray: ByteArray): TLVector =
    decodeFromBytes(ByteArrayInput(byteArray))

private fun TLExpressionDescriptor.decodeFromBytes(input: ByteArrayInput): TLExpression =
    when (this) {
        is TLTypeDescriptor -> decodeFromBytes(input)
        is TLVectorDescriptor -> decodeFromBytes(input)
        is TLBooleanDescriptor -> decodeFromBytes(input)
        is TLNullDescriptor -> decodeFromBytes(input)
        is TLIntDescriptor -> decodeFromBytes(input)
        is TLDoubleDescriptor -> TLDouble(input.readDouble())
        is TLStringDescriptor -> TLString(input.readString())
    }

private fun TLVectorDescriptor.decodeFromBytes(
    input: ByteArrayInput
): TLVector {
    val crc32 = input.readInt().toUInt()
    require(crc32 == TLVector.CRC32) { "CRC32 Mismatch: Trying to decode #${TLVector.CRC32.toString(radix = 16)} (vector), but got #${crc32.toString(radix = 16)}" }
    val size = input.readInt()
    val elements = List(size) { underlying.decodeFromBytes(input) }
    return TLVector(elements)
}

private fun TLTypeDescriptor.decodeFromBytes(
    input: ByteArrayInput
): TLConstructor {
    val crc32 = input.readInt().toUInt()

    val constructor = constructors.firstOrNull { constructor ->
        constructor.crc32 == crc32
    } ?: error("CRC32 Mismatch: Trying to decode type with constructors #${constructors.map { it.crc32.toString(radix = 16) }}, but got #${crc32.toString(radix = 16)}")

    val parameters = constructor.parameters.map { parameter ->
        parameter.decodeFromBytes(input)
    }

    return TLConstructor(crc32, parameters)
}

@Suppress("UnusedReceiverParameter")
private fun TLNullDescriptor.decodeFromBytes(input: ByteArrayInput): TLNull {
    val crc32 = input.readInt().toUInt()
    require(crc32 == TLNull.CRC32) { "CRC32 Mismatch: Trying to decode #${TLNull.CRC32.toString(radix = 16)} (Null), but got #${crc32.toString(radix = 16)}" }
    return TLNull
}

private fun TLBooleanDescriptor.decodeFromBytes(input: ByteArrayInput): TLBoolean {
    return when (val crc32 = input.readInt().toUInt()) {
        TLTrue.CRC32 -> TLTrue
        TLFalse.CRC32 -> TLFalse
        else -> error("CRC32 Mismatch: Trying to decode type with constructors #${constructors.map { it.crc32.toString(radix = 16) }} (Boolean), but got #${crc32.toString(radix = 16)}")
    }
}

private fun TLIntDescriptor.decodeFromBytes(input: ByteArrayInput): TLInt {
    val size = sizeBytes / Int.SIZE_BYTES
    val data = IntArray(size) { input.readInt() }
    return create(data)
}
