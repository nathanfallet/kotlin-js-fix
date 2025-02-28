package me.nathanfallet.kotlinjsinterfacefix.extensions

import org.gradle.api.provider.Property

interface KotlinJsInterfaceFixExtension {

    val flattenCjsExports: Property<Boolean>
    val exportJsInterfaces: Property<Boolean>
    val removeDoNotUseOrImplementIt: Property<Boolean>

}
