package kotl.core.element

public sealed interface TLConstructor : TLCallable, TLExpression {
    public data class Boxed(
        override val crc32: UInt,
        override val parameters: List<TLExpression>
    ) : TLConstructor

    public data class Bare(
        override val parameters: List<TLExpression>
    ) : TLConstructor {
        override val crc32: UInt? = null
    }
}

public fun TLConstructor(crc32: UInt?, parameters: List<TLExpression>): TLConstructor {
    if (crc32 == null) return TLConstructor.Bare(parameters)
    return TLConstructor.Boxed(crc32, parameters)
}
