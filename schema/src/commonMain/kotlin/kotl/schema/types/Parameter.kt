package kotl.schema.types

public data class Parameter(
    val name: ParameterName,
    val type: TypeReference,
    val nullabilityMarker: NullabilityMarker?
)
