import kotl.parser.ParserResult
import kotl.schema.element.TLSchemaCallable
import kotl.schema.parser.schemaParser
import kotl.schema.types.Parameter
import kotl.schema.types.TypeName
import kotl.schema.types.TypeParameter
import kotl.schema.types.TypeReference
import java.io.File

private fun main() {
    val parser = schemaParser()
    val file = File(System.getenv("user.dir"), "schema158.tl").apply {
        createNewFile()
    }

    val result = parser.parse(file.readText()) as ParserResult.Success

    for (element in result.value.filterIsInstance<TLSchemaCallable>()) {
        println(template(element))
        println()
        println()
    }
}

private fun template(
    function: TLSchemaCallable
) = """
@Serializable
@TLRpcCall(crc32 = 0x${function.hash?.string?.drop(n = 1)}_u)
data class ${function.name.string}${typeParamsTemplate(function.typeParameters)}(${paramsTemplate(function.parameters)})
""".trimIndent()

private fun typeParamsTemplate(
    parameters: List<TypeParameter>
) = buildString {
    if (parameters.isEmpty()) return ""
    append('<')
    for ((i, parameter) in parameters.withIndex()) {
        append(parameter.name.string)
        append(" : ")
        append(typeTemplate(parameter.upperBound))
        if (i != parameters.lastIndex) append(", ")
    }
    append('>')
}

private fun paramsTemplate(parameters: List<Parameter>): String = buildString {
    if (parameters.isEmpty()) return ""
    appendLine()
    append(
        buildString {
            for ((i, parameter) in parameters.withIndex()) {
                append("val ${parameter.name.string}: ${typeTemplate(parameter.type)}")
                if (i != parameters.lastIndex) appendLine(',')
            }
        }.prependIndent()
    )
    appendLine()
}

private fun typeTemplate(type: TypeReference): String = buildString {
    append(type.name.kotlinString)
    if (type.typeArguments.isNotEmpty()) {
        append('<')
        for ((i, argument) in type.typeArguments.withIndex()) {
            append(typeTemplate(argument.reference))
            if (i != type.typeArguments.lastIndex) append(", ")
        }
        append('>')
    }
}

private val TypeName.kotlinString: String get() = when (string) {
    "Type" -> "Any"
    "#" -> "Int"
    else -> string
}
