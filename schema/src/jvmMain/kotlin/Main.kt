import kotl.parser.ParserResult
import kotl.schema.parser.schemaParser
import kotl.schema.prettyPrint.prettyPrint
import java.io.File

private fun main() {
    val parser = schemaParser()
    val file = File(System.getenv("user.dir"), "schema158.tl").apply {
        createNewFile()
    }

    with (parser.parse(file.readText()) as ParserResult.Success) {
        value.forEach { element ->
            element.prettyPrint()
        }
        println()
        println("Remaining: \n$remaining")
    }
}
