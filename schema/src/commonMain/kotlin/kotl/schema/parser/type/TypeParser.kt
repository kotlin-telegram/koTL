package kotl.schema.parser.type

import kotl.parser.*
import kotl.schema.parser.line.emptyLineParser
import kotl.schema.types.TypeArgument
import kotl.schema.types.TypeReference
import kotl.schema.types.TypeName

internal fun ParserState.type(): TypeReference {
    val name = name()
    val arguments = arguments()
    return TypeReference(name, arguments)
}

private fun ParserState.name(): TypeName {
    val string = takeRegex(TypeName.Regex)
    return TypeName(string)
}

private fun ParserState.arguments(): List<TypeArgument> {
    return safely {
        consumeToken('<')
        val arguments = many(
            parser = parser { type() },
            separator = parser { comma() }
        ).map { reference -> TypeArgument(reference) }
        consumeToken('>')
        arguments
    }.orEmpty()
}
