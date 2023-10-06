package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLInt64(public val long: Long) : TLInt {
    override val data: IntArray get() {
        val lowerInt = long.toInt()
        val upperInt = (long shr 32).toInt()
        return intArrayOf(lowerInt, upperInt)
    }

    public companion object {
        public const val SIZE_BYTES: Int = Long.SIZE_BYTES
        public fun of(data: IntArray): TLInt64 {
            val long = (data[0].toLong() and 0xFFFFFFFF) or
                    (data[1].toLong() and 0xFFFFFFFF shl 32)
            return TLInt64(long)
        }
    }
}

public val Long.typedLanguage: TLInt64 get() = TLInt64(long = this)
