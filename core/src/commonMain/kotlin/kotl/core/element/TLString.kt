package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLString(
    public val string: String
) : TLPrimitive

public val String.typedLanguage: TLString get() = TLString(string = this)
