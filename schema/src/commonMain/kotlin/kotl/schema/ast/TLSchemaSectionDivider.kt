package kotl.schema.ast

public sealed interface TLSchemaSectionDivider : TLSchemaElement {
    public data object Functions : TLSchemaSectionDivider {
        override fun toString(): String = "TLSchemaSectionDivider.Functions"
    }
    public data object Types : TLSchemaSectionDivider {
        override fun toString(): String = "TLSchemaSectionDivider.Types"
    }
}
