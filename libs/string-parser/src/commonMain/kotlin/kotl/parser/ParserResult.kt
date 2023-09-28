package kotl.parser

public sealed interface ParserResult<out T> {
    public data class Success<out T>(
        val value: T,
        val remaining: String
    ) : ParserResult<T>
    public data object Failure : ParserResult<Nothing>
}
