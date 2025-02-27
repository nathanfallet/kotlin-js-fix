package me.nathanfallet.kotlinjsinterfacefix.extensions

import org.gradle.api.provider.Property

interface KotlinJsInterfaceFixExtension {

    val exportJsInterfaces: Property<Boolean>
    val removeDoNotUseOrImplementIt: Property<Boolean>

}
