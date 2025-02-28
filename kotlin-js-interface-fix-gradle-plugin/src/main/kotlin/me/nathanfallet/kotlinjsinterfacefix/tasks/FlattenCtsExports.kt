package me.nathanfallet.kotlinjsinterfacefix.tasks

import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class FlattenCtsExports : AbstractPostProcessingTask(".d.ts") {

    @TaskAction
    fun doAction() {
        getFilesToProcess().forEach(::processTypescriptFile)
    }

    private fun processTypescriptFile(file: File) {
        val oldLines = file.readLines()
        val newLines = mutableListOf<String>()
        var declarationLevel = 0

        oldLines.forEach { line ->
            if (
                line.trimStart().startsWith("export declare namespace") ||
                line.trimStart().startsWith("export as namespace") ||
                line.trimStart().startsWith("namespace")
            ) return@forEach
            var addExport = false

            // If the line contains the namespace (like `me.nathanfallet.sample.ClassName`), remove it
            val cleanedLine = line.replace(Regex("""([a-zA-Z0-9]+\.)+"""), "")

            val numberOfOpenBraces = cleanedLine.count { it == '{' }
            if (numberOfOpenBraces > 0) {
                if (declarationLevel == 0) addExport = true
                declarationLevel += numberOfOpenBraces
            }

            val numberOfCloseBraces = cleanedLine.count { it == '}' }
            if (numberOfCloseBraces > 0) {
                declarationLevel -= numberOfCloseBraces
                if (declarationLevel < 0) {
                    declarationLevel++
                    return@forEach
                }
            }

            newLines.add(if (addExport) "export declare $cleanedLine" else cleanedLine)
        }

        file.writeText(newLines.joinToString("\n"))
    }

}
