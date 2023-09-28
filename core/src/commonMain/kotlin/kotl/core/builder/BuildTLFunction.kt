package kotl.core.builder

import kotl.core.annotation.TLDsl
import kotl.core.element.TLElement
import kotl.core.element.TLFunction
import kotl.core.element.typedLanguage

@TLDsl
public class TLFunctionBuilder(private val crc32: UInt) {
    private val parameters = mutableListOf<TLElement>()

    public fun parameter(value: TLElement) {
        parameters += value
    }

    public fun parameter(boolean: Boolean): Unit = parameter(boolean.typedLanguage)
    public fun parameter(double: Double): Unit = parameter(double.typedLanguage)
    public fun parameter(int: Int): Unit = parameter(int.typedLanguage)
    public fun parameter(long: Long): Unit = parameter(long.typedLanguage)
    public fun parameter(nothing: Nothing?): Unit = parameter(nothing.typedLanguage)
    public fun parameter(string: String): Unit = parameter(string.typedLanguage)

    public inline fun functionParameter(crc32: UInt, builder: TLFunctionBuilder.() -> Unit = {}) {
        parameter(buildTLFunction(crc32, builder))
    }
    public inline fun vectorParameter(builder: TLVectorBuilder.() -> Unit) {
        parameter(buildTLVector(builder))
    }

    public fun build(): TLFunction = TLFunction(crc32, parameters.toList())
}

public inline fun buildTLFunction(
    crc32: UInt,
    block: TLFunctionBuilder.() -> Unit = {}
): TLFunction = TLFunctionBuilder(crc32).apply(block).build()
