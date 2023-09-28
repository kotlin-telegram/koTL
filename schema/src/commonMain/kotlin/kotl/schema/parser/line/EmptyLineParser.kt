package kotl.schema.parser.line

import kotl.parser.Parser
import kotl.parser.fail
import kotl.parser.parser
import kotl.parser.takeLine

internal fun emptyLineParser(): Parser<Nothing?> = parser {
    val line = takeLine()
    if(line.isNotBlank()) fail()
    null
}
