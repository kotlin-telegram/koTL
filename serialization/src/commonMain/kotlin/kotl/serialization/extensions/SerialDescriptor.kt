@file:OptIn(ExperimentalSerializationApi::class)

package kotl.serialization.extensions

import kotl.core.descriptor.*
import kotl.serialization.annotation.Crc32
import kotl.serialization.annotation.TLRpcCall
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*

internal val SerialDescriptor.tlRpcCall: TLRpcCall?
    get() = annotations.filterIsInstance<TLRpcCall>().firstOrNull()

internal val SerialDescriptor.crc32: Crc32?
    get() = annotations.filterIsInstance<Crc32>().firstOrNull()

public fun SerialDescriptor.asTLDescriptor(): TLExpressionDescriptor = when (kind) {
    PolymorphicKind.SEALED -> asTypeDescriptor()
    StructureKind.CLASS -> asTypeDescriptor()
    StructureKind.OBJECT -> asTypeDescriptor()
    StructureKind.LIST -> asVectorDescriptor()
    PrimitiveKind.BOOLEAN -> TLBooleanDescriptor
    PrimitiveKind.INT -> TLIntDescriptor
    PrimitiveKind.LONG -> TLLongDescriptor
    PrimitiveKind.DOUBLE -> TLDoubleDescriptor
    PrimitiveKind.STRING -> TLStringDescriptor
    PrimitiveKind.BYTE -> unsupportedPrimitive("Byte")
    PrimitiveKind.CHAR -> unsupportedPrimitive("Char")
    PrimitiveKind.FLOAT -> unsupportedPrimitive("Float")
    PrimitiveKind.SHORT -> unsupportedPrimitive("Short")
    SerialKind.ENUM -> error("TL doesn't support enums")
    StructureKind.MAP -> error("TL doesn't support maps")
    else -> error("Unsupported structure kind $kind")
}

private fun unsupportedPrimitive(
    name: String
): Nothing = throw SerializationException("TL doesn't support $name. All supported primitives are: Boolean, Int, Long, String, Double")

private fun SerialDescriptor.asTypeDescriptor(): TLTypeDescriptor {
    if (tlRpcCall != null) {
        error("Cannot create type descriptor for @RpcCall, it's not intended to be deserialized")
    }

    if (kind == PolymorphicKind.SEALED) {
        val sealed = getElementDescriptor(index = 1)
        val constructors = sealed.elementDescriptors.map { it.asTLDescriptor() }
            .flatMap { (it as TLTypeDescriptor).constructors }
        return TLTypeDescriptor(constructors)
    }

    val crc32 = crc32?.value ?: error("Your class should be annotated with @Crc32 for constructors or @TLRpcCall for functions to make it compatible with TL")
    val parameters = elementDescriptors.map { it.asTLDescriptor() }

    return TLTypeDescriptor(
        constructors = listOf(TLConstructorDescriptor(crc32, parameters))
    )
}

private fun SerialDescriptor.asVectorDescriptor(): TLVectorDescriptor {
    TODO()
}
