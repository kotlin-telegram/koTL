package kotl.schema.prettyPrint

import kotl.schema.element.TLSchemaComment
import kotl.schema.element.TLSchemaElement
import kotl.schema.element.TLSchemaCallable
import kotl.schema.element.TLSchemaSectionDivider
import kotl.schema.types.TypeReference

public fun TLSchemaElement.prettyString(): String = when (this) {
    is TLSchemaComment -> this.string
    is TLSchemaCallable -> buildString {
        append(name.string)
        if (hash != null) append(hash.string)
        append(' ')
        if (typeParameters.isNotEmpty()) {
            append('{')
            for (parameter in typeParameters) {
                append(parameter.name.string)
                append(':')
                append(parameter.upperBound.prettyString())
            }
            append('}')
        }
        append(' ')
        for (parameter in parameters) {
            append(parameter.name.string)
            append(':')
            append(parameter.type.prettyString())
            append(' ')
        }
        append("= ")
        append(returnType.prettyString())
        append(';')
    }
    is TLSchemaSectionDivider.Functions -> "---functions---"
    is TLSchemaSectionDivider.Types -> "---types---"
}

public fun TLSchemaElement.prettyPrint(): Unit = println(prettyString())

public fun TypeReference.prettyString(): String = buildString {
    append(name.string)
    if (typeArguments.isNotEmpty()) {
        append('<')
        for (argument in typeArguments) {
            append(argument.reference.prettyString())
        }
        append('>')
    }
}
