package kotl.serialization.annotation

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

/**
 * Annotate parameters that you want to be encoded as bare types
 * https://core.telegram.org/mtproto/serialize#boxed-and-bare-types
 *
 * You can use Bare<T> as an alternative
 */
@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class TLBare
