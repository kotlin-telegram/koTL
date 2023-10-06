package kotl.core.descriptor

import kotl.core.element.TLInt

public sealed interface TLIntDescriptor : TLPrimitiveDescriptor {
    public val sizeBytes: Int

    public fun create(data: IntArray): TLInt

    public companion object {
        public fun of(sizeBits: Int): TLIntDescriptor = when (sizeBits) {
            32 -> TLInt32Descriptor
            64 -> TLInt64Descriptor
            128 -> TLInt128Descriptor
            else -> error("Unsupported Int size")
        }
    }
}
