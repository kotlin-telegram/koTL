package kotl.core.element

public data class TLFunction(
    public val crc32: UInt,
    public val parameters: List<TLElement>
) : TLElement
