package kotl.schema.parser.section

import kotl.parser.*
import kotl.schema.element.TLSchemaSectionDivider

internal fun sectionDividerParser(): Parser<TLSchemaSectionDivider> =
    parser {
        consume("---")
        val sectionName = takeWhile { it != '-' }
        consume("---")

        when (sectionName) {
            "functions" -> TLSchemaSectionDivider.Functions
            "types" -> TLSchemaSectionDivider.Types
            else -> fail()
        }
    }
