package kotl.parser

public inline fun <T, R> Parser<T>.map(
    crossinline transform: (T) -> R
): Parser<R> = parser {
    transform(parse())
}
