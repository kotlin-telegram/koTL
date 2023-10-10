package kotl.core.builder

import kotl.core.annotation.TLDsl
import kotl.core.element.TLConstructor
import kotl.core.element.TLExpression
import kotl.core.element.TLFunction
import kotl.core.element.typedLanguage

@TLDsl
public class TLCallableBuilder(
    private val crc32: UInt? = null
) {
    private val parameters = mutableListOf<TLExpression>()

    public fun parameter(value: TLExpression) {
        parameters += value
    }

    public fun parameter(boolean: Boolean): Unit = parameter(boolean.typedLanguage)
    public fun parameter(double: Double): Unit = parameter(double.typedLanguage)
    public fun parameter(int: Int): Unit = parameter(int.typedLanguage)
    public fun parameter(long: Long): Unit = parameter(long.typedLanguage)
    public fun parameter(nothing: Nothing?): Unit = parameter(nothing.typedLanguage)
    public fun parameter(string: String): Unit = parameter(string.typedLanguage)

    public inline fun constructorParameter(crc32: UInt, builder: TLCallableBuilder.() -> Unit = {}) {
        parameter(buildTLConstructor(crc32, builder))
    }
    public inline fun vectorParameter(builder: TLVectorBuilder.() -> Unit) {
        parameter(buildTLVector(builder))
    }

    public fun toFunction(): TLFunction = TLFunction(
        crc32 = crc32 ?: error("Cannot create a function without crc32 set"),
        parameters = parameters.toList()
    )

    public fun toConstructor(): TLConstructor =
        if (crc32 == null) {
            TLConstructor.Bare(parameters)
        } else {
            TLConstructor.Boxed(crc32, parameters)
        }
}
