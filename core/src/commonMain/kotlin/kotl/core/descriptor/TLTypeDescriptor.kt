package kotl.core.descriptor

public sealed interface TLTypeDescriptor : TLExpressionDescriptor {
    public data class Boxed(val constructors: List<TLConstructorDescriptor>) : TLTypeDescriptor
    public data class Bare(val parameters: List<TLExpressionDescriptor>) : TLTypeDescriptor
}

public fun TLTypeDescriptor(constructors: List<TLConstructorDescriptor>): TLTypeDescriptor =
    TLTypeDescriptor.Boxed(constructors)
