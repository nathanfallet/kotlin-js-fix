package me.nathanfallet.kotlinjsinterfacefix.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
import java.io.File

abstract class AbstractPostProcessingTask(
    @Internal
    val extension: String,
) : DefaultTask() {

    @get:InputFiles
    abstract val inputFiles: ConfigurableFileCollection

    init {
        inputs.files(inputFiles)
        outputs.files(getFilesToProcess())
        outputs.upToDateWhen { false } // Avoid having UP-TO-DATE tasks since we replace files from other tasks
    }

    @Internal
    protected fun getFilesToProcess(): List<File> = inputFiles.flatMap { file ->
        file.walk().filter { it.name.endsWith(extension) }
    }

}
