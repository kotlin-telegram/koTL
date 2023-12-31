package kotl.core.element

public data class TLFunction(
    override val crc32: UInt,
    override val parameters: List<TLExpression>
) : TLCallable
