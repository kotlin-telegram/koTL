package kotl.core.descriptor

public data class TLConstructorDescriptor(
    val crc32: UInt,
    val parameters: List<TLExpressionDescriptor>
)
