package kotl.parser

public typealias Consumer = Parser<Unit>

public fun ParserState.consume(string: String): String {
    if (remaining.startsWith(string)) {
        discard(n = string.length)
        return string
    } else {
        fail()
    }
}

public fun ParserState.consume(char: Char): Char {
    if (remaining.startsWith(char)) {
        discard(n = 1)
        return char
    } else {
        fail()
    }
}

public fun ParserState.consumeToken(string: String): String {
    whitespace()
    consume(string)
    whitespace()
    return string
}

public fun ParserState.consumeToken(char: Char): Char {
    whitespace()
    consume(char)
    whitespace()
    return char
}
