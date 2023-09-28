package kotl.schema.parser.comment.multiline

import kotl.parser.*
import kotl.schema.element.TLSchemaComment

internal fun multilineCommentParser(): Parser<TLSchemaComment> = parser {
    val message = buildString {
        append(commentStart())
        val body = many(
            multilineCommentParser().map { it.string },
            parser {
                fail { commentEnd() }
                take(n = 1)
            }
        ).joinToString(separator = "")
        append(body)
        append(commentEnd())
    }

    TLSchemaComment(message)
}

private fun ParserState.commentStart(): String = consume("/*")
private fun ParserState.commentEnd(): String = consume("*/")
