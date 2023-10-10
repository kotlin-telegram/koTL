package kotl.core.element

public sealed interface TLCallable : TLElement {
    public val crc32: UInt?
    public val parameters: List<TLExpression>
}

public val TLCallable.bare: Boolean get() = crc32 == null
public val TLCallable.boxed: Boolean get() = crc32 != null
