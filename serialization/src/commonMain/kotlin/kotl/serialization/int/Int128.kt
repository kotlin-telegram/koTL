package kotl.serialization.int

import kotl.serialization.annotation.TLSize
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class Int128(
    @TLSize(bits = 128)
    public val data: IntArray
) {
    init {
        require(data.size == 4) { "int128 expected, but int${data.size * Int.SIZE_BITS} got" }
    }
}
