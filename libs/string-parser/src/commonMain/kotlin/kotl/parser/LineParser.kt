package kotl.parser

public inline fun <T> lineParser(
    crossinline block: ParserDSL<T>
): Parser<T> = parser {
    val value = block()
    semicolon()
    value
}
