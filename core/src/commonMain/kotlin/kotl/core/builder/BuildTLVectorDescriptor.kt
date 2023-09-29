package kotl.core.builder

import kotl.core.descriptor.*

public class TLVectorDescriptorBuilder {
    private var underlying: TLExpressionDescriptor? = null

    public fun underlying(expression: TLExpressionDescriptor) {
        underlying = expression
    }

    public fun booleanUnderlying(): Unit = underlying(TLBooleanDescriptor)
    public fun doubleUnderlying(): Unit = underlying(TLDoubleDescriptor)
    public fun intUnderlying(): Unit = underlying(TLIntDescriptor)
    public fun longUnderlying(): Unit = underlying(TLLongDescriptor)
    public fun stringUnderlying(): Unit = underlying(TLStringDescriptor)
    public fun nullUnderlying(): Unit = underlying(TLNullDescriptor)

    public fun underlying(block: TLTypeDescriptorBuilder.() -> Unit) {
        underlying(buildTLTypeDescriptor(block))
    }
    public fun underlyingVector(block: TLVectorDescriptorBuilder.() -> Unit) {
        underlying(buildTLVectorDescriptor(block))
    }

    public fun build(): TLVectorDescriptor = TLVectorDescriptor(
        underlying = underlying ?: error("You should call `underlying` function in order to build TLVectorDescriptor")
    )
}

public inline fun buildTLVectorDescriptor(
    block: TLVectorDescriptorBuilder.() -> Unit
): TLVectorDescriptor = TLVectorDescriptorBuilder().apply(block).build()
