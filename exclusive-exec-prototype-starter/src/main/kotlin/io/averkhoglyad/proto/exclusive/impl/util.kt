package io.averkhoglyad.proto.exclusive.impl

import io.averkhoglyad.proto.exclusive.annotation.Exclusive
import java.lang.reflect.Method
import java.util.concurrent.locks.Lock

internal fun collectKeyArguments(method: Method, args: Array<Any?>): Array<Any?> {
    return method.parameterAnnotations
        .asSequence()
        .mapIndexed { i, el -> if (el.any { it is Exclusive.Key }) i else -1 }
        .filter { it >= 0 }
        .map { args[it] }
        .toList()
        .takeUnless { it.isEmpty() }
        ?.toTypedArray()
        ?: args
}

@Throws(Throwable::class)
internal fun Lock.executeExclusively(fn: () -> Any?): Any? {
    var allowedExecute = false
    if (this.tryLock()) {
        allowedExecute = true
    } else {
        this.lock()
    }
    try {
        if (allowedExecute) {
            return fn()
        }
    } finally {
        this.unlock()
    }
    return null // TODO: Return value from the first and single execution
}
