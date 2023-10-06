package kotl.core.descriptor

import kotl.core.element.TLInt32

public data object TLInt32Descriptor : TLIntDescriptor {
    override val sizeBytes: Int = TLInt32.SIZE_BYTES
    override fun create(data: IntArray): TLInt32 = TLInt32.of(data)
}
