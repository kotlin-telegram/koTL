package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLInt32(public val int: Int) : TLInt {
    override val data: IntArray get() = intArrayOf(int)

    public companion object {
        public const val SIZE_BYTES: Int = Int.SIZE_BYTES
        public fun of(data: IntArray): TLInt32 = TLInt32(data[0])
    }
}

public val Int.typedLanguage: TLInt32 get() = TLInt32(int = this)
