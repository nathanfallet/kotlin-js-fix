# kotlin-js-interface-fix

[![License](https://img.shields.io/github/license/nathanfallet/kotlin-js-interface-fix)](LICENSE)
[![Issues](https://img.shields.io/github/issues/nathanfallet/kotlin-js-interface-fix)]()
[![Pull Requests](https://img.shields.io/github/issues-pr/nathanfallet/kotlin-js-interface-fix)]()
[![Code Size](https://img.shields.io/github/languages/code-size/nathanfallet/kotlin-js-interface-fix)]()

A simple Gradle plugin to fix Kotlin/JS interfaces issues.

It allows to export Kotlin/JS interfaces to JavaScript, and use them in TypeScript without __doNotUseOrImplementIt.

The idea was to fix [KT-56618](https://youtrack.jetbrains.com/issue/KT-56618),
[KT-75584](https://youtrack.jetbrains.com/issue/KT-75584) and [KT-75592](https://youtrack.jetbrains.com/issue/KT-75592)
easily, without having to wait for JetBrains to fix it in the Compiler.

## Add the plugin to your project

Simply add the gradle plugin to your `build.gradle(.kts)`:

```kotlin
plugins {
    id("me.nathanfallet.kotlinjsinterfacefix") version "1.0.1"
}
```

You can optionally configure which feature of the plugin is enabled (default is all of them):

```kotlin
kotlinjsinterfacefix { // The block is optional if you want to use the default values
    flattenCjsExports = false // Optional, default is false / Only for CommonJS
    exportJsInterfaces = true // Optional, default is true
    removeDoNotUseOrImplementIt = true // Optional, default is true
}
```

## Example

We will show you what the plugin does with the following Kotlin/JS code:

```kotlin
@JsExport
interface MyInterface {
    fun doSomething()
}

@JsExport
class MyClass : MyInterface {
    override fun doSomething() {
        println("Hello, world!")
    }
}
```

### Without the plugin

Here are the generated JavaScript and TypeScript code:

```javascript
initMetadataForInterface(MyInterface, 'MyInterface');
initMetadataForClass(MyClass, 'MyClass', MyClass, VOID, [MyInterface]);

function MyClass() {
}

protoOf(MyClass).doSomething = function () {
    println('Hello, world!');
};

function MyInterface() {
}

export {
    MyClass as MyClass,
};
```

```typescript
type Nullable<T> = T | null | undefined

export declare class MyClass implements MyInterface {
    constructor();

    doSomething(): void;

    readonly __doNotUseOrImplementIt: MyInterface["__doNotUseOrImplementIt"];
}

export declare interface MyInterface {
    doSomething(): void;

    readonly __doNotUseOrImplementIt: {
        readonly "me.nathanfallet.sample.MyInterface": unique symbol;
    };
}
```

You can see that in the JavaScript code, the `MyInterface` is not exported, and in the TypeScript code, the
`__doNotUseOrImplementIt` is present.

### With the plugin

```javascript
initMetadataForInterface(MyInterface, 'MyInterface');
initMetadataForClass(MyClass, 'MyClass', MyClass, VOID, [MyInterface]);

function MyClass() {
}

protoOf(MyClass).doSomething = function () {
    println('Hello, world!');
};

function MyInterface() {
}

export {
    MyClass as MyClass,
    MyInterface as MyInterface,
};
```

```typescript
type Nullable<T> = T | null | undefined

export declare class MyClass implements MyInterface {
    constructor();

    doSomething(): void;
}

export declare interface MyInterface {
    doSomething(): void;
}
```

Now, the `MyInterface` is exported in the JavaScript code, and the `__doNotUseOrImplementIt` is removed from the
TypeScript code.
