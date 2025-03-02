package me.nathanfallet.kotlinjsfix.tasks

import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ExportMjsInterfaces : AbstractPostProcessingTask(".mjs") {

    @TaskAction
    fun doAction() {
        getFilesToProcess().forEach(::processJavascriptFile)
    }

    private fun processJavascriptFile(file: File) {
        val oldLines = file.readLines()
        val newLines = mutableListOf<String>()
        val interfaces = mutableSetOf<String>()
        val existingExports = mutableSetOf<String>()
        var exportFound = false
        var exportDone = false

        val addMissingExports = {
            interfaces.forEach {
                // Check if it's not already exported, or file was already processed
                if (existingExports.contains(it)) return@forEach
                existingExports.add(it)
                newLines.add("  $it as $it,")
            }
            exportDone = true // Don't do it again (in case of multiple export blocks)
        }

        oldLines.forEachIndexed { index, line ->
            // Detect interfaces
            val match = Regex("""^initMetadataForInterface\((\w+),""").find(line)
            if (match != null) interfaces.add(match.groupValues[1])

            // Detect export block
            if (line.startsWith("export {")) {
                exportFound = true
            } else if (exportFound && !exportDone) {
                if (line.startsWith("};")) addMissingExports()
                else line.replace(",", "").trim().split(" ").firstOrNull()?.let { existingExports.add(it) }
            }

            newLines.add(line)
        }

        // If export block is not done, add it at the end
        if (!exportDone && interfaces.isNotEmpty()) {
            newLines.add("export {")
            addMissingExports()
            newLines.add("};")
        }

        file.writeText(newLines.joinToString("\n"))
    }

}
