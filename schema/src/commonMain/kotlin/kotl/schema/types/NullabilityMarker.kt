package kotl.schema.types

public data class NullabilityMarker(
    val reference: ParameterName,
    val shift: Int
)
