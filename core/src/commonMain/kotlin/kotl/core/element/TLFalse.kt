package kotl.core.element

public data object TLFalse : TLBoolean {
    override val boolean: Boolean = false

    public const val CRC32: UInt = 0xbc799737_u
}
