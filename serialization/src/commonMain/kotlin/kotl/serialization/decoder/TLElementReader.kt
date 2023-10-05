package kotl.serialization.decoder

import kotl.core.element.TLConstructor
import kotl.core.element.TLElement
import kotl.core.element.TLVector
import kotl.core.element.typedLanguage

internal interface TLElementReader {
    fun isDone(): Boolean
    fun nextTLElement(): TLElement
}

internal class SingleElementReader(private val value: TLElement) : TLElementReader {
    private var isDone = false
    override fun isDone(): Boolean = isDone
    override fun nextTLElement(): TLElement {
        if (isDone()) throw NoSuchElementException()
        isDone = true
        return value
    }
}

internal class ListElementReader(
    value: TLVector
) : TLElementReader {
    private val iterator = value.elements.iterator()

    override fun isDone(): Boolean = !iterator.hasNext()
    override fun nextTLElement(): TLElement = iterator.next()
}

internal class ConstructorElementReader(
    value: TLConstructor
) : TLElementReader {
    private val iterator = value.parameters.iterator()

    override fun isDone(): Boolean = !iterator.hasNext()
    override fun nextTLElement(): TLElement = iterator.next()
}

internal class SealedElementReader(
    className: String,
    constructor: TLConstructor
) : TLElementReader {
    private val iterator = listOf(className.typedLanguage, constructor).iterator()

    override fun isDone(): Boolean = !iterator.hasNext()
    override fun nextTLElement(): TLElement = iterator.next()
}
