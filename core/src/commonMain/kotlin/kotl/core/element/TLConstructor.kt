package kotl.core.element

public data class TLConstructor(
    override val crc32: UInt,
    override val parameters: List<TLExpression>
) : TLCallable, TLExpression
