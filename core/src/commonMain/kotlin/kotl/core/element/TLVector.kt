package kotl.core.element

public data class TLVector(val elements: List<TLExpression>) : TLExpression {
    public fun asTLConstructor(): TLConstructor =
        TLConstructor(
            crc32 = CRC32,
            parameters = listOf(elements.size.typedLanguage) + elements
        )

    public companion object {
        public const val CRC32: UInt = 0x1cb5c415_u

        public val Empty: TLVector = TLVector(emptyList())
    }
}
