package kotl.parser.debug

import kotl.parser.ParserState

public fun ParserState.printDebugInfo(tag: String? = null) {
    println("""
        PARSER STATE${if (tag == null) "" else " ($tag)"}:
        remaining: 
    """.trimIndent() + remaining + '\n')
}
