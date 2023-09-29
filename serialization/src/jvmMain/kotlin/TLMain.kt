package kotl.serialization

import kotl.core.builder.buildTLTypeDescriptor
import kotl.core.descriptor.*
import kotl.serialization.annotation.Crc32
import kotl.serialization.annotation.TLRpcCall
import kotl.serialization.extensions.asTLDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToByteArray

@Serializable
@TLRpcCall(crc32 = 0x2d84d5f5_u)
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

private fun main() {
    val descriptor = InputUserType.serializer().descriptor.asTLDescriptor()
    println(descriptor.prettyString())

    val manual = buildTLTypeDescriptor {
        constructor(0xb98886cf_u)
        constructor(0xf7c1b13f_u)
        constructor(0xf21158c6_u) {
            longParameter()
            longParameter()
            stringParameter()
        }
    }
    println(manual.prettyString())
}

private fun TLExpressionDescriptor.prettyString(indent: String = ""): String = when (this) {
    TLBooleanDescriptor -> indent + "boolean" + '\n'
    TLDoubleDescriptor -> indent + "double" + '\n'
    TLIntDescriptor -> indent + "int" + '\n'
    TLLongDescriptor -> indent + "long" + '\n'
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

private fun main1() {
    val users = listOf(
        inputUserEmpty,
        inputUserSelf,
        inputUser(userId = 0xff, accessHash = 0xff, username = "name")
    )
    val request = GetUserRequest(users)

    println("INITIAL: " + TL.encodeToTLElement(GetUserRequest.serializer(), request))
    val bytes = TL.encodeToByteArray(request)
    println("BYTES: ${bytes.toHexString()}")
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun ByteArray.toHexString() =
    asUByteArray().joinToString(" ") { it.toString(16).padStart(2, '0') }
