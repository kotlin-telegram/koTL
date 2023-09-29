package kotl.core.element

public data object TLNull : TLPrimitive {
    public fun asTLConstructor(): TLConstructor = TLConstructor(CRC32, emptyList())

    public const val CRC32: UInt = 0x56730bcc_u
}

@Suppress("UnusedReceiverParameter")
public val Nothing?.typedLanguage: TLNull get() = TLNull
