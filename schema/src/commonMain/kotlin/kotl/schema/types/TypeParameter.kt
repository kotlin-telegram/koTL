package kotl.schema.types

public data class TypeParameter(
    val name: ParameterName,
    val upperBound: TypeReference
)
