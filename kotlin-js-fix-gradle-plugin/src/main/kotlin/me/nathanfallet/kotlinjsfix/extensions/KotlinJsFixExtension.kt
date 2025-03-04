package me.nathanfallet.kotlinjsfix.extensions

import org.gradle.api.provider.Property

interface KotlinJsFixExtension {

    val flattenCjsExports: Property<Boolean>
    val exportJsInterfaces: Property<Boolean>
    val removeDoNotUseOrImplementIt: Property<Boolean>

}
