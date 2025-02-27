package me.nathanfallet.kotlinjsinterfacefix.tasks

import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ExportJsInterfaces : AbstractPostProcessingTask(".mjs") {

    @TaskAction
    fun doAction() {
        getFilesToProcess().forEach(::processJavascriptFile)
    }

    private fun processJavascriptFile(file: File) {
        val oldLines = file.readLines()
        val newLines = mutableListOf<String>()
        val interfaces = mutableSetOf<String>()
        var exportFound = false
        var exportDone = false

        oldLines.forEachIndexed { index, line ->
            // Detect interfaces
            val match = Regex("""^initMetadataForInterface\((\w+),""").find(line)
            if (match != null) interfaces.add(match.groupValues[1])

            // Detect export block
            if (line.startsWith("export {")) exportFound = true
            else if (exportFound && !exportDone && line.startsWith("};")) {
                interfaces.forEach { newLines.add("  $it as $it,") }
                exportDone = true // Don't do it again (in case of multiple export blocks)
            }

            newLines.add(line)
        }

        // If export block is not found, add it at the end
        if (!exportFound && interfaces.isNotEmpty()) {
            newLines.add("export {")
            interfaces.forEach { newLines.add("  $it as $it,") }
            newLines.add("};")
        }

        // Write the modified content back
        file.writeText(newLines.joinToString("\n"))
    }

}
