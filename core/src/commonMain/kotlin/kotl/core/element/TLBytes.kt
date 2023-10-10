package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLBytes(
    public val bytes: ByteArray
) : TLPrimitive
