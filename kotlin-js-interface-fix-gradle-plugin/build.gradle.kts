plugins {
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.2.1"
}

repositories {
    gradlePluginPortal()
}

kotlin {
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.10")
    }
}

group = "me.nathanfallet.kotlinjsinterfacefix"
version = "1.0.1"

gradlePlugin {
    website = "https://github.com/nathanfallet/kotlin-js-interface-fix"
    vcsUrl = "https://github.com/nathanfallet/kotlin-js-interface-fix.git"

    plugins {
        create("kotlin-js-interface-fix-gradle-plugin") {
            id = "me.nathanfallet.kotlinjsinterfacefix"
            implementationClass = "me.nathanfallet.kotlinjsinterfacefix.KotlinJsInterfaceFix"
            displayName = "A fix for Kotlin/JS interfaces"
            description =
                "Allows to export Kotlin/JS interfaces to JavaScript, and use them in TypeScript without __doNotUseOrImplementIt"
            tags = listOf("kotlin", "js", "interface", "fix")
        }
    }
}
