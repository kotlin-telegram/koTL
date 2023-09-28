package kotl.parser

public inline fun ParserState.takeWhile(predicate: (Char) -> Boolean): String {
    val string = remaining.takeWhile(predicate)
    discard(string.length)
    return string
}

public inline fun ParserState.takeUntil(string: String): String {
    return many {
        fail { consume(string) }
        take(n = 1)
    }.joinToString(separator = "")
}

public fun ParserState.takeLine(
    includeNewline: Boolean = false
): String {
    val line = takeWhile { char -> char != '\n' }
    take(n = 1)
    return if (includeNewline) (line + '\n') else line
}

public fun ParserState.takeRegex(regex: Regex): String {
    val match = regex.find(remaining) ?: fail()
    if (match.range.first != 0) fail()
    val string = match.value
    discard(string.length)
    return string
}

public fun ParserState.take(n: Int): String {
    val string = remaining.take(n)
    discard(n)
    return string
}

public fun ParserState.takeInt(): Int = takeWhile { it.isDigit() }
    .toIntOrNull()
    ?: fail()
