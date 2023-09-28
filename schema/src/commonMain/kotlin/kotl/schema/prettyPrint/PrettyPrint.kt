package kotl.schema.prettyPrint

import kotl.schema.ast.TLSchemaComment
import kotl.schema.ast.TLSchemaElement
import kotl.schema.ast.TLSchemaFunction
import kotl.schema.ast.TLSchemaSectionDivider
import kotl.schema.types.TypeReference

public fun TLSchemaElement.prettyString(): String = when (this) {
    is TLSchemaComment -> this.string
    is TLSchemaFunction -> buildString {
        append(name.string)
        if (hash != null) append(hash.string)
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
    is TLSchemaSectionDivider.Types -> "---functions---"
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
