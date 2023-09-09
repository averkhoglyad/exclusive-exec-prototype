package io.averkhoglyad.proto.exclusive.impl

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.locks.ReentrantLock

class ExclusiveExecutionMethodInterceptor : MethodInterceptor {

    private val locks: ConcurrentMap<Key, ReentrantLock> = ConcurrentHashMap()

    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        val key = buildExecutionKey(invocation)
        val lock = locks.computeIfAbsent(key) { _ -> ReentrantLock() }
        val result = lock.executeExclusively { invocation.proceed() }
        locks.remove(key, lock)
        return result
    }

    private fun buildExecutionKey(invocation: MethodInvocation): Key {
        return Key(invocation.getThis()!!, invocation.method.toString(), collectKeyArguments(invocation.method, invocation.arguments))
    }
}
