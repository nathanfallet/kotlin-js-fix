# kotlin-js-fix

[![License](https://img.shields.io/github/license/nathanfallet/kotlin-js-fix)](LICENSE)
[![Issues](https://img.shields.io/github/issues/nathanfallet/kotlin-js-fix)]()
[![Pull Requests](https://img.shields.io/github/issues-pr/nathanfallet/kotlin-js-fix)]()
[![Code Size](https://img.shields.io/github/languages/code-size/nathanfallet/kotlin-js-fix)]()

A simple Gradle plugin to fix for Kotlin/JS compiler issues.

It allows to temporarily fix Kotlin/JS compiler, like export interfaces to JS and use them in TypeScript without __
doNotUseOrImplementIt.

The idea was to fix [KT-56618](https://youtrack.jetbrains.com/issue/KT-56618),
[KT-75584](https://youtrack.jetbrains.com/issue/KT-75584) and [KT-75592](https://youtrack.jetbrains.com/issue/KT-75592)
easily, without having to wait for JetBrains to fix it in the Compiler.

## Add the plugin to your project

Simply add the gradle plugin to your `build.gradle(.kts)`:

```kotlin
plugins {
    id("me.nathanfallet.kotlinjsfix") version "1.0.1"
}
```

You can optionally configure which feature of the plugin is enabled:

```kotlin
kotlinjsfix {
    flattenCjsExports = false // Optional, default is false / Only for CommonJS
    exportJsInterfaces = true // Optional, default is false
    removeDoNotUseOrImplementIt = true // Optional, default is false
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
