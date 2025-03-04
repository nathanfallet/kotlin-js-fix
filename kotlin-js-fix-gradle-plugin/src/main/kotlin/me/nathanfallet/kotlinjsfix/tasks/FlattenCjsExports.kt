package me.nathanfallet.kotlinjsfix.tasks

import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class FlattenCjsExports : AbstractPostProcessingTask(".js", ".cjs") {

    @TaskAction
    fun doAction() {
        getFilesToProcess().forEach(::processJavascriptFile)
    }

    private fun processJavascriptFile(file: File) {
        val oldLines = file.readLines()
        val newLines = mutableListOf<String>()
        var insideExportSection = false

        oldLines.forEach { line ->
            if (line.contains("function \$jsExportAll\$(_)")) {
                insideExportSection = true
                newLines.add(line)
                return@forEach // Don't process this line (or it will break it)
            }
            if (line.trim() == "}") insideExportSection = false // We are done with the export section
            if (!insideExportSection) {
                newLines.add(line)
                return@forEach
            }

            // Apply rule 1: If the line starts with `var $namespace`, remove the line
            if (line.trimStart().startsWith("var $")) return@forEach

            // Apply rule 2: If the line contains `$namespace`, replace it with `_`
            val cleanedLine = line.replace(Regex("""(\$\w+)+"""), "_")
            newLines.add(cleanedLine)
        }

        file.writeText(newLines.joinToString("\n"))
    }

}
