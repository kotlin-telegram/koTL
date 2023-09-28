package kotl.serialization

import kotl.core.builder.buildTLFunction
import kotl.core.encoder.encodeToByteArray
import kotl.serialization.annotation.TLFunction
import kotl.serialization.annotation.TLType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToByteArray

@Serializable
@TLFunction(crc32 = 0x2d84d5f5_u)
public data class GetUserRequest(
    val ids: List<InputUserType>
)

@Serializable
@TLType
public sealed interface InputUserType

@Serializable
@TLFunction(crc32 = 0xb98886cf_u)
public data object inputUserEmpty : InputUserType

@Serializable
@TLFunction(crc32 = 0xf7c1b13f_u)
public data object inputUserSelf : InputUserType

@Serializable
@TLFunction(crc32 = 0xf21158c6_u)
public data class inputUser(
    val userId: Long,
    val accessHash: Long
) : InputUserType

private fun main() {
    val users = listOf(
        inputUserEmpty,
        inputUserSelf,
        inputUser(userId = 0xff, accessHash = 0xff)
    )
    val request = GetUserRequest(users)
    println(TL.encodeToTLElement(GetUserRequest.serializer(), request))
    val bytes = TL.encodeToByteArray(request)

    val request2 = buildTLFunction(/* getUserRequest */0x2d84d5f5_u) {
        vectorParameter {
            addFunctionCall(/* inputUserEmpty */0xb98886cf_u)
            addFunctionCall(/* inputUserSelf */0xf7c1b13f_u)
            addFunctionCall(/* inputUser */0xf21158c6_u) {
                parameter(/* userId */0xffL)
                parameter(/* accessHash */0xffL)
            }
        }
    }

    println(bytes.toHexString())
    println(request2.encodeToByteArray().toHexString())
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun ByteArray.toHexString() =
    asUByteArray().joinToString(" ") { it.toString(16).padStart(2, '0') }
