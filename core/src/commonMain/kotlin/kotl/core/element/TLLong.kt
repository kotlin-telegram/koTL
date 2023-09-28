package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLLong(public val long: Long) : TLPrimitive

public val Long.typedLanguage: TLLong get() = TLLong(long = this)
