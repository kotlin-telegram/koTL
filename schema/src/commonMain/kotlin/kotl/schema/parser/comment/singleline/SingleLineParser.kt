package kotl.schema.parser.comment.singleline

import kotl.parser.*
import kotl.schema.ast.TLSchemaComment
import kotl.schema.ast.TLSchemaElement

internal fun singleLineCommentParser() = parser {
    val string = commentStart() + takeLine()
    TLSchemaComment(string)
}

private fun ParserState.commentStart() = consume(string = "//")
