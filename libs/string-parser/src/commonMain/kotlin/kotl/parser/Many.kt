package kotl.parser

public fun <T> manyParser(
    vararg parsers: Parser<T>
): Parser<List<T>> = parser {
    many(*parsers)
}

public fun <T> ParserState.many(
    vararg parsers: Parser<T>
): List<T> = many {
    any(*parsers)
}

public fun <T> ParserState.many(
    parser: ParserState.() -> T
): List<T> = many(parser(parser))

public fun <T> ParserState.many(
    parser: Parser<T>
): List<T> {
    val result = mutableListOf<T>()

    while (true) {
        parser.tryParse()
            .onSuccess { value ->
                result += value
            }
            .onFailure { return result }
    }
}

public fun <T> ParserState.many(
    parser: Parser<T>,
    separator: Parser<*>
): List<T> {
    val first = parser
        .tryParse()
        .getOrElse { return emptyList() }

    val parserWithSeparator = parser {
        separator.parse()
        parser.parse()
    }

    return listOf(first) + many(parserWithSeparator)
}
