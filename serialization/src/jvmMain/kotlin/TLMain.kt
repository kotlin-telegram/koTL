@file:Suppress("ArrayInDataClass")

package kotl.serialization

import kotl.core.descriptor.*
import kotl.serialization.annotation.Crc32
import kotl.serialization.annotation.TLSize
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray

@Serializable
@Crc32(value = 0x2d84d5f5_u)
public data class GetUserRequest(
    val ids: List<InputUserType>
)

@Serializable
public sealed interface InputUserType

@Serializable
@Crc32(value = 0xb98886cf_u)
public data object inputUserEmpty : InputUserType

@Serializable
@Crc32(value = 0xf7c1b13f_u)
public data object inputUserSelf : InputUserType

@Serializable
@Crc32(value = 0xf21158c6_u)
public data class inputUser(
    val userId: Long,
    val accessHash: Long,
    val username: String
) : InputUserType

@Serializable
@Crc32(value = 0xf21158c6_u)
public data class ExchangePQ(
    @TLSize(bits = 128)
    public val nonce: IntArray
)

//private fun main() {
//    val descriptor = InputUserType.serializer().descriptor.asTLDescriptor()
//    println(descriptor.prettyString())
//
//    val manual = buildTLTypeDescriptor {
//        constructor(0xb98886cf_u)
//        constructor(0xf7c1b13f_u)
//        constructor(0xf21158c6_u) {
//            longParameter()
//            longParameter()
//            stringParameter()
//        }
//    }
//    println(manual.prettyString())
//}

private fun TLExpressionDescriptor.prettyString(indent: String = ""): String = when (this) {
    TLBooleanDescriptor -> indent + "boolean" + '\n'
    TLDoubleDescriptor -> indent + "double" + '\n'
    TLInt32Descriptor -> indent + "int" + '\n'
    TLInt64Descriptor -> indent + "long" + '\n'
    TLInt128Descriptor -> indent + "int128" + '\n'
    TLNullDescriptor -> indent + "null" + '\n'
    TLStringDescriptor -> indent + "string" + '\n'
    is TLTypeDescriptor -> buildString {
        appendLine(indent + "type: ")
        constructors.forEach { constructor ->
            appendLine("$indent    constructor: ${constructor.crc32}")
            for (parameter in constructor.parameters) {
                append(parameter.prettyString(indent = "$indent        "))
            }
        }
    }
    is TLVectorDescriptor -> buildString {
        appendLine(indent + "vector of: ")
        append(underlying.prettyString(indent = "$indent    "))
    }
}

private fun main() {
    val users = listOf(
        inputUserEmpty,
        inputUserSelf,
        inputUser(userId = 0, accessHash = 0xff, username = "name")
    )
    val request = GetUserRequest(users)

    println("INITIAL: $request")
    val bytes = TL.encodeToByteArray(request)
    println("BYTES: ${bytes.toHexString()}")
    val deserialized: GetUserRequest = TL.decodeFromByteArray(bytes)
    println("RESULT: $deserialized")

    println()
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun ByteArray.toHexString() =
    asUByteArray().joinToString(" ") { it.toString(16).padStart(2, '0') }
