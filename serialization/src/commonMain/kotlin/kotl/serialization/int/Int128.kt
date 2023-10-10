package kotl.serialization.int

import kotl.serialization.annotation.TLSize
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public data class Int128(
    @TLSize(bits = 128)
    public val data: IntArray
) {
    init {
        require(data.size == 4) { "int128 expected, but int${data.size * Int.SIZE_BITS} got" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Int128) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
