package kotl.parser

public fun <T> anyParser(
    vararg parsers: Parser<T>
): Parser<T> = parser {
    any(*parsers)
}

public fun <T> anyParser(
    parsers: List<Parser<T>>
): Parser<T> = parser {
    any(parsers)
}

public fun <T> ParserState.any(
    vararg parsers: Parser<T>
): T = any(parsers.toList())

public fun <T> ParserState.any(
    parsers: List<Parser<T>>
): T {
    for (parser in parsers) {
        parser
            .tryParse()
            .onSuccess { return it }
    }
    fail()
}
