pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "kotlin-js-interface-fix"
includeBuild("kotlin-js-interface-fix-gradle-plugin")

include(":sample-package")
