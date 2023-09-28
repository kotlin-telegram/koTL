package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLInt(public val int: Int) : TLPrimitive

public val Int.typedLanguage: TLInt get() = TLInt(int = this)
