package kotl.core.descriptor

import kotl.core.element.TLNull

public data object TLNullDescriptor : TLPrimitiveDescriptor {
    public fun asTypeDescriptor(): TLTypeDescriptor =
        TLTypeDescriptor(listOf(TLNullConstructorDescriptor))
}

public val TLNullConstructorDescriptor: TLConstructorDescriptor =
    TLConstructorDescriptor(TLNull.CRC32, emptyList())
