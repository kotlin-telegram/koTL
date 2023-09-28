package kotl.schema.parser

import kotl.parser.*
import kotl.schema.element.TLSchemaElement
import kotl.schema.parser.comment.commentParser
import kotl.schema.parser.function.functionParser
import kotl.schema.parser.line.emptyLineParser
import kotl.schema.parser.section.sectionDividerParser

internal fun schemaParser(): Parser<List<TLSchemaElement>> {
    return parser {
        many(
            functionParser(),
            sectionDividerParser(),
            commentParser(),
            emptyLineParser()
        ).filterNotNull()
    }
}
