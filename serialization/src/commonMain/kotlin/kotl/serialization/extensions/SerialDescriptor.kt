@file:OptIn(ExperimentalSerializationApi::class)

package kotl.serialization.extensions

import kotl.core.descriptor.*
import kotl.serialization.annotation.Crc32
import kotl.serialization.annotation.TLBare
import kotl.serialization.annotation.TLRpc
import kotl.serialization.annotation.TLSize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.*

internal val SerialDescriptor.tlRpc: TLRpc?
    get() = annotations.filterIsInstance<TLRpc>().firstOrNull()

internal val SerialDescriptor.crc32: Crc32?
    get() = annotations.filterIsInstance<Crc32>().firstOrNull()


public fun SerialDescriptor.asTLDescriptor(
    annotations: List<Annotation> = this.annotations
): TLExpressionDescriptor = when (kind) {
    PolymorphicKind.SEALED,
    StructureKind.OBJECT,
    StructureKind.CLASS -> {
        if (isInline) {
            getElementDescriptor(index = 0).asTLDescriptor(getElementAnnotations(index = 0))
        } else {
            asTypeDescriptor(annotations)
        }
    }
    StructureKind.OBJECT -> asTypeDescriptor(annotations)
    StructureKind.LIST -> asVectorDescriptor(annotations)
    PrimitiveKind.BOOLEAN -> TLBooleanDescriptor
    PrimitiveKind.INT -> TLInt32Descriptor
    PrimitiveKind.LONG -> TLInt64Descriptor
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

private fun SerialDescriptor.asTypeDescriptor(
    annotations: List<Annotation>
): TLTypeDescriptor {
    if (tlRpc != null) {
        error("Cannot create type descriptor for @RpcCall, it's not intended to be deserialized")
    }

    if (kind == PolymorphicKind.SEALED) {
        val sealed = getElementDescriptor(index = 1)
        val constructors = sealed.elementDescriptors.map { descriptor ->
            descriptor.asTLDescriptor()
        }.flatMap { (it as TLTypeDescriptor.Boxed).constructors }
        return TLTypeDescriptor(constructors)
    }

    val crc32 = if (annotations.any { it is TLBare }) {
        null
    } else {
        this.crc32?.value ?: error("Your class $serialName should be annotated with @Crc32 for constructors or @TLRpcCall for functions to make it compatible with TL")
    }

    val parameters = elementDescriptors.mapIndexed { i, descriptor ->
        descriptor.asTLDescriptor(getElementAnnotations(i))
    }

    crc32 ?: return TLTypeDescriptor.Bare(parameters)

    return TLTypeDescriptor.Boxed(
        constructors = listOf(
            element = TLConstructorDescriptor(crc32, parameters)
        )
    )
}

private fun SerialDescriptor.asVectorDescriptor(annotations: List<Annotation>): TLExpressionDescriptor {
    val elementDescriptor = getElementDescriptor(index = 0)

    if (elementDescriptor.kind == PrimitiveKind.INT) {
        val size = annotations.filterIsInstance<TLSize>().firstOrNull()
        if (size != null) return TLIntDescriptor.of(size.bits)
    }

    if (elementDescriptor.kind == PrimitiveKind.BYTE) {
        return TLBytesDescriptor
    }

    val underlying = elementDescriptor.asTLDescriptor()
    return TLVectorDescriptor(underlying)
}
