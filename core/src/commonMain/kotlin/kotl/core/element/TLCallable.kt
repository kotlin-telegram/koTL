package kotl.core.element

public sealed interface TLCallable : TLElement {
    public val crc32: UInt
    public val parameters: List<TLExpression>
}
