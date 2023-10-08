package kotl.core.element

import kotlin.jvm.JvmInline

@JvmInline
public value class TLInt128(override val data: IntArray) : TLInt {
    init {
        require(data.size == SIZE_BYTES / Int.SIZE_BYTES)
    }

    public companion object {
        public const val SIZE_BYTES: Int = 16
    }
}
