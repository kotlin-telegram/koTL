package kotl.schema.parser.comment

import kotl.parser.Parser
import kotl.parser.anyParser
import kotl.schema.element.TLSchemaComment
import kotl.schema.parser.comment.multiline.multilineCommentParser
import kotl.schema.parser.comment.singleline.singleLineCommentParser

internal fun commentParser(): Parser<TLSchemaComment> {
    return anyParser(
        singleLineCommentParser(),
        multilineCommentParser()
    )
}
