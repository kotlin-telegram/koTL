package kotl.core.element

public sealed interface TLBoolean : TLPrimitive {
    public val boolean: Boolean
}

public val Boolean.typedLanguage: TLBoolean get() = when (this) {
    true -> TLTrue
    false -> TLFalse
}
