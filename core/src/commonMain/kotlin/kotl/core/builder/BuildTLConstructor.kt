package kotl.core.builder

import kotl.core.element.TLConstructor

public inline fun buildTLConstructor(
    crc32: UInt? = null,
    builder: TLCallableBuilder.() -> Unit = {}
): TLConstructor = TLCallableBuilder(crc32).apply(builder).toConstructor()
