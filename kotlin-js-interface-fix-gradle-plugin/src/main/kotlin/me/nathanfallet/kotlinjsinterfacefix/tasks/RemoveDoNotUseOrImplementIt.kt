package me.nathanfallet.kotlinjsinterfacefix.tasks

import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class RemoveDoNotUseOrImplementIt : AbstractPostProcessingTask(".d.ts") {

    @TaskAction
    fun doAction() {
        getFilesToProcess().forEach(::processTypescriptFile)
    }

    // Taken from https://youtrack.jetbrains.com/issue/KT-56618
    private fun processTypescriptFile(file: File) {
        val oldLines = file.readLines()
        val newLines = ArrayList<String>()
        var toSkip = false
        for (line in oldLines) {
            if (line.contains("__doNotUseOrImplementIt")) {
                // If the line also contains a semicolon you can skip only this line and continue on. If it doesn't have one
                // Keep looking until the declaration is closed
                toSkip = !line.contains(';')
                continue
            } else if (toSkip) {
                // After reaching a curly brace semicolon, this skipping is over.
                toSkip = !line.contains("};")
                continue
            }
            newLines.add(line)
        }
        file.writeText(newLines.joinToString("\n"))
    }

}
