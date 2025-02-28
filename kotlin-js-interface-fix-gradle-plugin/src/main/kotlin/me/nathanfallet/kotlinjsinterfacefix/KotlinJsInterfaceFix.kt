package me.nathanfallet.kotlinjsinterfacefix

import me.nathanfallet.kotlinjsinterfacefix.extensions.KotlinJsInterfaceFixExtension
import me.nathanfallet.kotlinjsinterfacefix.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

abstract class KotlinJsInterfaceFix : Plugin<Project> {

    private val buildGroup = "kotlinjsinterfacefix"

    override fun apply(project: Project) {
        project.configureExtensions()
        project.afterEvaluate { configureTasks() }
    }

    private fun Project.configureExtensions() {
        val extension = extensions.create<KotlinJsInterfaceFixExtension>("kotlinjsinterfacefix")
        extension.flattenCjsExports.convention(false)
        extension.exportJsInterfaces.convention(true)
        extension.removeDoNotUseOrImplementIt.convention(true)
    }

    private fun Project.configureTasks() {
        // Register our tasks to run every time a Kotlin2JsCompile task is run
        val extension = project.extensions.getByType<KotlinJsInterfaceFixExtension>()
        tasks.forEach { task ->
            if (task !is Kotlin2JsCompile) return@forEach

            if (extension.flattenCjsExports.get()) {
                setupTask<FlattenCjsExports>(task.name, "flattenCjsExports")
                setupTask<FlattenCtsExports>(task.name, "flattenCtsExports")
            }
            if (extension.exportJsInterfaces.get()) {
                setupTask<ExportMjsInterfaces>(task.name, "exportMjsInterfaces")
                // TODO: ExportCjsInterfaces (for CommonJS, with .js/.cjs extension)
            }
            if (extension.removeDoNotUseOrImplementIt.get()) {
                setupTask<RemoveDoNotUseOrImplementIt>(task.name, "removeDoNotUseOrImplementIt")
            }
        }
    }

    private inline fun <reified T : AbstractPostProcessingTask> Project.setupTask(
        compileTask: String,
        name: String,
    ) {
        val taskName = name + "For" + compileTask.replaceFirstChar { it.uppercase() }
        tasks.register<T>(taskName) {
            group = buildGroup
            dependsOn(compileTask)
            if (name == "exportCjsInterfaces") {
                val flattenTaskName = "flattenCjsExportsFor" + compileTask.replaceFirstChar { it.uppercase() }
                if (tasks.names.contains(flattenTaskName)) dependsOn(flattenTaskName)
            }
            inputFiles.from(project.tasks.named(compileTask).get().outputs.files)
        }
        tasks.named(compileTask) {
            finalizedBy(project.tasks.named(taskName))
        }
    }

}
