package kotl.serialization.encoder

import kotl.core.descriptor.TLIntDescriptor
import kotl.core.element.*

internal interface TLElementWriter {
    fun writeElement(element: Any?)

    fun endStructure() {}
}

internal class SingleElementWriter : TLElementWriter {
    lateinit var encoded: TLElement

    override fun writeElement(element: Any?) {
        require(element is TLElement) { "Only TLElement supported for this writer, got $element" }
        encoded = element
    }
}

internal class BytesElementWriter(
    private val parent: TLElementWriter
) : TLElementWriter {
    private val bytes = mutableListOf<Byte>()

    override fun writeElement(element: Any?) {
        require(element is Byte) { "Only kotlin.Byte supported for this writer, got $element" }
        bytes += element
    }

    override fun endStructure() {
        val element = TLBytes(bytes.toByteArray())
        parent.writeElement(element)
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

    override fun writeElement(element: Any?) {
        require(element is Int) { "Only kotlin.Int supported for this writer, got $element" }
        ints[index++] = element
    }

    override fun endStructure() {
        val element = descriptor.create(ints)
        parent.writeElement(element)
    }
}

internal class ListElementWriter(
    private val parent: TLElementWriter
) : TLElementWriter {
    private var encoded = TLVector.Empty

    override fun writeElement(element: Any?) {
        require(element is TLExpression) { "Cannot write $element to vector, only expressions are allowed" }
        encoded = encoded.copy(elements = encoded.elements + element)
    }

    override fun endStructure() = parent.writeElement(encoded)
}

internal class ConstructorElementWriter(
    crc32: UInt?,
    private val parent: TLElementWriter
) : TLElementWriter {
    private var encoded = TLConstructor(crc32, emptyList())

    override fun writeElement(element: Any?) {
        require(element is TLExpression) { "Cannot write $element to constructor, only expressions are allowed" }
        encoded = encoded.copy(parameters = encoded.parameters + element)
    }

    override fun endStructure() = parent.writeElement(encoded)
}

private fun TLConstructor.copy(parameters: List<TLExpression>): TLConstructor =
    when (this) {
        is TLConstructor.Bare -> copy(parameters = parameters)
        is TLConstructor.Boxed -> copy(parameters = parameters)
    }

internal class FunctionElementWriter(
    crc32: UInt,
    private val parent: TLElementWriter
) : TLElementWriter {
    private var encoded = TLFunction(crc32, emptyList())

    override fun writeElement(element: Any?) {
        require(element is TLExpression) { "Cannot write $element to function, only expressions are allowed" }
        encoded = encoded.copy(parameters = encoded.parameters + element)
    }

    override fun endStructure() = parent.writeElement(encoded)
}
