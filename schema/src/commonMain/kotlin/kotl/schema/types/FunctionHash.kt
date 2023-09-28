package kotl.schema.types

import kotlin.jvm.JvmInline

@JvmInline
public value class FunctionHash(public val string: String) {
    init {
        require(string.matches(Regex)) { "Function hash should be hash mark ( # ) and exactly 8 hexadecimal digits" }
    }

    public companion object {
        public val Regex: Regex = Regex("#[a-f0-9]+")
    }
}
