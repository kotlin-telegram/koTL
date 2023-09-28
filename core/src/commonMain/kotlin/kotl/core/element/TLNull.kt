package kotl.core.element

public data object TLNull : TLPrimitive {
    public const val CRC32: UInt = 0x56730bcc_u
}

@Suppress("UnusedReceiverParameter")
public val Nothing?.typedLanguage: TLNull get() = TLNull
