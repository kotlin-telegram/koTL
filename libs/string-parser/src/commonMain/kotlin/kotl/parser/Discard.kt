package kotl.parser

public fun ParserState.discard(n: Int) {
    try {
        remaining = remaining.substring(n)
    } catch (_: Throwable) {
        fail()
    }
}
