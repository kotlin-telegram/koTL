package kotl.serialization.bare

import kotl.serialization.annotation.TLBare
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Use with parameters that you want to be encoded as bare types
 * https://core.telegram.org/mtproto/serialize#boxed-and-bare-types
 *
 * You can use @TLBare as an alternative
 */
@Serializable
@JvmInline
public value class Bare<T>(
    @TLBare
    public val value: T
)

public val <T> T.bare: Bare<T> get() = Bare(value = this)
