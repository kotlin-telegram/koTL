package kotl.core.element

public data class TLVector(val elements: List<TLElement>) : TLElement {
    public companion object {
        public const val CRC32: UInt = 0x1cb5c415_u
        public val Empty: TLVector = TLVector(emptyList())
    }
}
