package kotl.core.builder

import kotl.core.element.TLFunction

public inline fun buildTLFunction(
    crc32: UInt,
    block: TLCallableBuilder.() -> Unit = {}
): TLFunction = TLCallableBuilder(crc32).apply(block).toFunction()
