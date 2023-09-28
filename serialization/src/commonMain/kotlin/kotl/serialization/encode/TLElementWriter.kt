package kotl.serialization.encode

import kotl.core.element.TLElement
import kotl.core.element.TLFunction
import kotl.core.element.TLVector

internal interface TLElementWriter {
    val encoded: TLElement

    fun writeTLElement(element: TLElement)

    fun endStructure() {}
}

internal class SingleElementWriter : TLElementWriter {
    override lateinit var encoded: TLElement

    override fun writeTLElement(element: TLElement) {
        encoded = element
    }
}

internal class ListElementWriter(
    private val parent: TLElementWriter
) : TLElementWriter {
    override var encoded = TLVector.Empty

    override fun writeTLElement(element: TLElement) {
        encoded = encoded.copy(elements = encoded.elements + element)
    }

    override fun endStructure() = parent.writeTLElement(encoded)
}

internal class FunctionElementWriter(
    crc32: UInt,
    private val parent: TLElementWriter
) : TLElementWriter {
    override var encoded = TLFunction(crc32, emptyList())

    override fun writeTLElement(element: TLElement) {
        encoded = encoded.copy(parameters = encoded.parameters + element)
    }

    override fun endStructure() = parent.writeTLElement(encoded)
}
