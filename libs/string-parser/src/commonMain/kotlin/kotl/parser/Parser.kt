package kotl.parser

public fun interface Parser<out T> {
    public fun parse(string: String): ParserResult<T>
}
