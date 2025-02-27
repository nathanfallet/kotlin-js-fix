package me.nathanfallet.sample

import kotlin.js.JsExport

@JsExport
class MyClass : MyInterface {

    override fun doSomething() {
        println("Hello, world!")
    }

}
