package kotl.schema.types

public data class TypeReference(
    val name: TypeName,
    val typeArguments: List<TypeArgument>
)
