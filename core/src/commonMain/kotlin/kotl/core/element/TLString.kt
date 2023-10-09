package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLString(
    public val bytes: ByteArray
) : TLPrimitive {
    public val string: String get() = bytes.decodeToString()
}

public val String.typedLanguage: TLString get() = TLString(encodeToByteArray())
