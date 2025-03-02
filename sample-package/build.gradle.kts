plugins {
    kotlin("multiplatform")
    id("me.nathanfallet.kotlinjsfix")
}

kotlin {
    // jvm & js
    jvmToolchain(21)
    jvm {
        withJava()
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
    }
    js {
        useEsModules()
        generateTypeScriptDefinitions()
        binaries.library()
        nodejs()
        browser()
    }

    applyDefaultHierarchyTemplate()
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
    }
}

kotlinjsfix {
    flattenCjsExports = false // Optional, default is false
    exportJsInterfaces = true // Optional, default is false
    removeDoNotUseOrImplementIt = true // Optional, default is false
}
