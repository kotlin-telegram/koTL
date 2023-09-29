package kotl.schema.element

import kotl.schema.types.*

public data class TLSchemaCallable(
    val name: FunctionName,
    val hash: FunctionHash?,
    val typeParameters: List<TypeParameter>,
    val parameters: List<Parameter>,
    val returnType: TypeReference
) : TLSchemaElement
