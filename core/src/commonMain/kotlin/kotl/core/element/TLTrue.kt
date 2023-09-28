package kotl.core.element

public data object TLTrue : TLBoolean {
    override val boolean: Boolean = true

    public const val CRC32: UInt = 0x997275b5_u
}
