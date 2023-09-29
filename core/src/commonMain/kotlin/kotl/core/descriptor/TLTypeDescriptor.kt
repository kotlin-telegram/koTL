package kotl.core.descriptor

public data class TLTypeDescriptor(
    val constructors: List<TLConstructorDescriptor>
) : TLExpressionDescriptor
