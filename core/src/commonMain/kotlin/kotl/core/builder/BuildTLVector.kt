package kotl.core.builder

import kotl.core.annotation.TLDsl
import kotl.core.element.TLElement
import kotl.core.element.TLExpression
import kotl.core.element.TLVector
import kotl.core.element.typedLanguage

@TLDsl
public class TLVectorBuilder {
    private val elements = mutableListOf<TLExpression>()

    public fun add(element: TLExpression) {
        elements += element
    }

    public fun add(boolean: Boolean): Unit = add(boolean.typedLanguage)
    public fun add(double: Double): Unit = add(double.typedLanguage)
    public fun add(int: Int): Unit = add(int.typedLanguage)
    public fun add(long: Long): Unit = add(long.typedLanguage)
    public fun add(nothing: Nothing?): Unit = add(nothing.typedLanguage)
    public fun add(string: String): Unit = add(string.typedLanguage)

    public inline fun addFunctionCall(
        crc32: UInt,
        block: TLCallableBuilder.() -> Unit = {}
    ) {
        add(buildTLConstructor(crc32, block))
    }
    public inline fun addVector(block: TLVectorBuilder.() -> Unit) {
        add(buildTLVector(block))
    }

    public fun build(): TLVector = TLVector(elements.toList())
}

public inline fun buildTLVector(
    block: TLVectorBuilder.() -> Unit
): TLVector = TLVectorBuilder().apply(block).build()
