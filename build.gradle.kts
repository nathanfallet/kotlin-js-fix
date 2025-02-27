plugins {
    kotlin("multiplatform").version("2.1.10").apply(false)
    kotlin("plugin.serialization").version("2.1.10").apply(false)
    id("org.jetbrains.kotlinx.kover").version("0.8.0").apply(false)
    id("com.google.devtools.ksp").version("2.1.10-1.0.30").apply(false)
    id("com.vanniktech.maven.publish").version("0.30.0").apply(false)
}

allprojects {
    group = "me.nathanfallet.kotlinjsinterfacefix"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }
}
