package kotl.schema.types

import kotlin.jvm.JvmInline

@JvmInline
public value class FunctionName(
    public val string: String
) {
    public val namespace: String get() = string.substringBefore('.')
    public val simpleName: String get() = string.substringAfter('.')

    init {
        require(string.matches(Regex)) { "Declaration name should be an alphanumeric string" }
    }

    public companion object {
        public val Regex: Regex = Regex("[a-zA-Z][a-zA-Z0-9_]*(\\.[a-zA-Z0-9_]+)*")
    }
}
