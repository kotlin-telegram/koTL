package kotl.schema.types

import kotlin.jvm.JvmInline

@JvmInline
public value class ParameterName(
    public val string: String
) {
    init {
        require(string.matches(Regex)) { "Declaration name should be an alphanumeric string" }
    }

    public companion object {
        public val Regex: Regex = Regex("[a-zA-Z][a-zA-Z0-9_]*")
    }
}
