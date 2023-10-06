package kotl.core.builder

import kotl.core.descriptor.*

public class TLConstructorDescriptorBuilder(
    private val crc32: UInt
) {
    private val expressions = mutableListOf<TLExpressionDescriptor>()

    public fun parameter(expression: TLExpressionDescriptor) {
        expressions += expression
    }

    public fun booleanParameter(): Unit = parameter(TLBooleanDescriptor)
    public fun doubleParameter(): Unit = parameter(TLDoubleDescriptor)
    public fun int32Parameter(): Unit = parameter(TLInt32Descriptor)
    public fun int64Parameter(): Unit = parameter(TLInt64Descriptor)
    public fun int128Parameter(): Unit = parameter(TLInt128Descriptor)
    public fun stringParameter(): Unit = parameter(TLStringDescriptor)
    public fun nullParameter(): Unit = parameter(TLNullDescriptor)

    public fun parameter(block: TLTypeDescriptorBuilder.() -> Unit) {
        parameter(buildTLTypeDescriptor(block))
    }

    public fun vectorParameter(block: TLVectorDescriptorBuilder.() -> Unit) {
        parameter(buildTLVectorDescriptor(block))
    }

    public fun build(): TLConstructorDescriptor = TLConstructorDescriptor(crc32, expressions)
}

public inline fun buildTLConstructorDescriptor(
    crc32: UInt,
    block: TLConstructorDescriptorBuilder.() -> Unit = {}
): TLConstructorDescriptor = TLConstructorDescriptorBuilder(crc32).apply(block).build()
