package kotl.parser

public fun ParserState.whitespace(
    newlines: Boolean = true
) {
    if (newlines) {
        takeWhile { char -> char.isWhitespace() }
    } else {
        takeWhile { char -> char.isWhitespace() && char != '\n' }
    }
}
