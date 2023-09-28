package kotl.schema.ast

import kotl.schema.types.*

public data class TLSchemaFunction(
    val name: FunctionName,
    val hash: FunctionHash?,
    val parameters: List<Parameter>,
    val returnType: TypeReference
) : TLSchemaElement
