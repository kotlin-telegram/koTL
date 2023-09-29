package kotl.core.builder

import kotl.core.descriptor.TLConstructorDescriptor
import kotl.core.descriptor.TLTypeDescriptor

public class TLTypeDescriptorBuilder {
    private val constructors = mutableListOf<TLConstructorDescriptor>()

    public fun constructor(value: TLConstructorDescriptor) {
        constructors += value
    }

    public fun constructor(
        crc32: UInt,
        block: TLConstructorDescriptorBuilder.() -> Unit = {}
    ) {
        constructor(buildTLConstructorDescriptor(crc32, block))
    }

    public fun build(): TLTypeDescriptor {
        require(constructors.isNotEmpty()) { "Cannot create a type descriptor without any constructors" }
        return TLTypeDescriptor(constructors)
    }
}

public inline fun buildTLTypeDescriptor(
    block: TLTypeDescriptorBuilder.() -> Unit
): TLTypeDescriptor = TLTypeDescriptorBuilder().apply(block).build()
