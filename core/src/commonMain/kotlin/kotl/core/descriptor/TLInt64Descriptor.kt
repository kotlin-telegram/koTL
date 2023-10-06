package kotl.core.descriptor

import kotl.core.element.TLInt64

public data object TLInt64Descriptor : TLIntDescriptor {
    override val sizeBytes: Int = TLInt64.SIZE_BYTES
    override fun create(data: IntArray): TLInt64 = TLInt64.of(data)
}
