package kotl.parser

public inline fun <T> ParserState.safely(
    crossinline block: ParserState.() -> T
): T? = parser(block)
    .tryParse()
    .getOrNull()
