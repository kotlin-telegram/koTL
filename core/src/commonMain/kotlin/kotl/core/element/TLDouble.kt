package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLDouble(public val double: Double) : TLPrimitive

public val Double.typedLanguage: TLDouble get() = TLDouble(double = this)
