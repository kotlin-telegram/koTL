package kotl.schema.parser.function

import kotl.parser.*
import kotl.schema.element.TLSchemaFunction
import kotl.schema.parser.type.type
import kotl.schema.types.*
import kotl.schema.types.ParameterName

internal fun functionParser(): Parser<TLSchemaFunction> = lineParser {
    val name = name()
    val hash = hash()
    val parameters = parameters()
    safely { consumeToken('?') }
    consumeToken('=')
    val returnType = type()

    TLSchemaFunction(
        name = name,
        parameters = parameters,
        hash = hash,
        returnType = returnType
    )
}

private fun ParserState.name(): FunctionName {
    val declaration = takeRegex(FunctionName.Regex)
    return FunctionName(declaration)
}

private fun ParserState.hash(): FunctionHash? {
    val hash = safely { consume('#') } ?: return null
    val string = hash + takeWhile { char -> !char.isWhitespace() }
    whitespace()
    return FunctionHash(string)
}

private fun ParserState.parameters(): List<Parameter> {
    return many(
        parser = parser {
            val name = parameterName()
            colon()
            val nullabilityMarker = nullabilityMarker()
            val type = type()
            Parameter(name, type, nullabilityMarker)
        },
        separator = parser { whitespace() }
    )
}

private fun ParserState.parameterName(): ParameterName {
    val declaration = takeRegex(ParameterName.Regex)
    return ParameterName(declaration)
}

private fun ParserState.nullabilityMarker(): NullabilityMarker? {
    return safely {
        val reference = parameterName()
        consume('.')
        val shift = takeInt()
        consume('?')
        NullabilityMarker(reference, shift)
    }
}
