package kotl.parser

public typealias ParserDSL<T> = ParserState.() -> T

public class ParserState(public var remaining: String) {
    public fun <T> Parser<T>.parse(): T {
        return when (val result = parse(remaining)) {
            ParserResult.Failure -> fail()
            is ParserResult.Success -> {
                remaining = result.remaining
                result.value
            }
        }
    }

    public fun <T> Parser<T>.tryParse(): Result<T> {
        return try {
            Result.success(parse())
        } catch (failure: ParserFailure) {
            Result.failure(failure)
        }
    }
}

public class ParserFailure() : RuntimeException()

public inline fun ParserState.fail(crossinline dsl: ParserDSL<*>) {
    fail(parser(dsl))
}

/**
 * Fail if parser succeeded
 */
public fun ParserState.fail(parser: Parser<*>) {
    parser
        .tryParse()
        .onSuccess { fail() }
}

@Suppress("UnusedReceiverParameter")
public fun ParserState.fail(): Nothing {
    throw ParserFailure()
}

public inline fun <T> parser(
    crossinline block: ParserState.() -> T
): Parser<T> {
    return Parser { tokens ->
        val context = ParserState(tokens)
        try {
            val result = context.block()
            ParserResult.Success(result, context.remaining)
        } catch (failure: ParserFailure) {
            ParserResult.Failure
        }
    }
}
