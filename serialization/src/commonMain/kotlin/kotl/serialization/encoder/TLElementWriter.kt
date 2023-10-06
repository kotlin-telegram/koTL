package kotl.serialization.encoder

import kotl.core.descriptor.TLIntDescriptor
import kotl.core.element.*

internal interface TLElementWriter {
    fun writeTLElement(element: TLElement)

    fun endStructure() {}
}

internal class SingleElementWriter : TLElementWriter {
    lateinit var encoded: TLElement

    override fun writeTLElement(element: TLElement) {
        encoded = element
    }
}

internal class IntElementWriter(
    private val parent: TLElementWriter,
    private val descriptor: TLIntDescriptor
) : TLElementWriter {
    private val ints = IntArray(
        size = descriptor.sizeBytes / Int.SIZE_BYTES
    )
    private var index = 0

    override fun writeTLElement(element: TLElement) {
        ints[index++] = (element as? TLInt32)?.int ?: error("Only int elements supported for this writer")
    }

    override fun endStructure() {
        val element = descriptor.create(ints)
        parent.writeTLElement(element)
    }
}

internal class ListElementWriter(
    private val parent: TLElementWriter
) : TLElementWriter {
    private var encoded = TLVector.Empty

    override fun writeTLElement(element: TLElement) {
        require(element is TLExpression) { "Cannot write $element to vector, only expressions are allowed" }
        encoded = encoded.copy(elements = encoded.elements + element)
    }

    override fun endStructure() = parent.writeTLElement(encoded)
}

internal class ConstructorElementWriter(
    crc32: UInt,
    private val parent: TLElementWriter
) : TLElementWriter {
    private var encoded = TLConstructor(crc32, emptyList())

    override fun writeTLElement(element: TLElement) {
        require(element is TLExpression) { "Cannot write $element to constructor, only expressions are allowed" }
        encoded = encoded.copy(parameters = encoded.parameters + element)
    }

    override fun endStructure() = parent.writeTLElement(encoded)
}

internal class FunctionElementWriter(
    crc32: UInt,
    private val parent: TLElementWriter
) : TLElementWriter {
    private var encoded = TLFunction(crc32, emptyList())

    override fun writeTLElement(element: TLElement) {
        require(element is TLExpression) { "Cannot write $element to function, only expressions are allowed" }
        encoded = encoded.copy(parameters = encoded.parameters + element)
    }

    override fun endStructure() = parent.writeTLElement(encoded)
}
