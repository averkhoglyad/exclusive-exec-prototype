package io.averkhoglyad.proto.exclusive

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.ClassFilter
import org.springframework.aop.support.DynamicMethodMatcherPointcut
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

class ExclusiveExecutionMethodInterceptor : MethodInterceptor {

    private val locks: ConcurrentMap<Key, ReentrantLock> = ConcurrentHashMap()

    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        val key = buildExcutionKey(invocation)
        val lock = locks.computeIfAbsent(key) { _ -> ReentrantLock() }
        val result = doExecute(lock, invocation)
        locks.remove(key, lock)
        return result
    }

    private fun buildExcutionKey(invocation: MethodInvocation): Key {
        return Key(invocation.`this`!!, invocation.method.toString(), collectKeyArgs(invocation))
    }

    private fun collectKeyArgs(invocation: MethodInvocation): Array<Any?> {
        val parameterAnnotations = invocation.method.parameterAnnotations

        val keyArgs = parameterAnnotations.asSequence()
            .mapIndexed { i, el ->
                if (el.any { it is Exclusive.Key }) {
                    i
                } else {
                    -1
                }
            }
            .filter { it >= 0 }
            .map { invocation.arguments[it] }
            .toList()

        if (keyArgs.isEmpty()) {
            return invocation.arguments
        } else {
            return keyArgs.toTypedArray()
        }
    }

    @Throws(Throwable::class)
    private fun doExecute(lock: ReentrantLock, invocation: MethodInvocation): Any? {
        var allowedExecute = false
        if (lock.tryLock()) {
            allowedExecute = true
        } else {
            lock.lock()
        }
        try {
            if (allowedExecute) {
                invocation.proceed()
            }
        } finally {
            lock.unlock()
        }
        return null // TODO: Return value from the single
    }
}

class ExclusiveExecutionPointcut(private val targetPackage: String) : DynamicMethodMatcherPointcut() {

    override fun matches(method: Method, targetClass: Class<*>, vararg args: Any?): Boolean {
        return targetClass.kotlin.hasAnnotation<Exclusive>() || method.declaredAnnotations.any { it is Exclusive }
    }

    override fun getClassFilter(): ClassFilter {
        return ClassFilter {
            it.packageName.startsWith(targetPackage) && (it.kotlin.hasAnnotation<Exclusive>() || it.kotlin.declaredFunctions.any { fn -> fn.hasAnnotation<Exclusive>() })
        }
    }
}
