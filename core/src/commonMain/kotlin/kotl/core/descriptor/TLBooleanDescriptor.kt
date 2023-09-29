package kotl.core.descriptor

import kotl.core.element.TLFalse
import kotl.core.element.TLTrue

public data object TLBooleanDescriptor : TLPrimitiveDescriptor {
    public val constructors: List<TLConstructorDescriptor> = listOf(
        TLTrueConstructorDescriptor,
        TLFalseConstructorDescriptor
    )
    public fun asTypeDescriptor(): TLTypeDescriptor = TLTypeDescriptor(constructors)
}

public val TLTrueConstructorDescriptor: TLConstructorDescriptor =
    TLConstructorDescriptor(TLTrue.CRC32, emptyList())

public val TLFalseConstructorDescriptor: TLConstructorDescriptor =
    TLConstructorDescriptor(TLFalse.CRC32, emptyList())
