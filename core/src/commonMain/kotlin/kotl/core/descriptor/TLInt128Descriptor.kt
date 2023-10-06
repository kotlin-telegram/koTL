package kotl.core.descriptor

import kotl.core.element.TLInt128

public data object TLInt128Descriptor : TLIntDescriptor {
    override val sizeBytes: Int = 16
    override fun create(data: IntArray): TLInt128 = TLInt128(data)
}
