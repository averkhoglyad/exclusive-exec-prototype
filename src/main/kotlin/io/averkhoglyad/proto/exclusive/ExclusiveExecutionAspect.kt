package io.averkhoglyad.proto.exclusive

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.locks.ReentrantLock

@Aspect
class ExclusiveExecutionAspect {

    private val locks: ConcurrentMap<Key, ReentrantLock> = ConcurrentHashMap()

    @Around("Pointcuts.exclusiveExecution()")
    @Throws(Throwable::class)
    fun execute(joinPoint: ProceedingJoinPoint): Any? {
        val key = buildExecutionKey(joinPoint)
        val lock = locks.computeIfAbsent(key) { _ -> ReentrantLock() }
        val result = doExecute(lock, joinPoint)
        locks.remove(key, lock)
        return result
    }

    private fun buildExecutionKey(joinPoint: ProceedingJoinPoint): Key {
        return Key(joinPoint.getThis(), joinPoint.signature.toShortString(), collectKeyArgs(joinPoint))
    }

    private fun collectKeyArgs(joinPoint: ProceedingJoinPoint): Array<Any?> {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val parameterAnnotations = method.parameterAnnotations

        val keyArgs = parameterAnnotations.asSequence()
            .mapIndexed { i, el ->
                if (el.any { it is Exclusive.Key }) {
                    i
                } else {
                    -1
                }
            }
            .filter { it >= 0 }
            .map { joinPoint.args[it] }
            .toList()
        
        if (keyArgs.isEmpty()) {
            return joinPoint.args
        } else {
            return keyArgs.toTypedArray()
        }
    }

    @Throws(Throwable::class)
    private fun doExecute(lock: ReentrantLock, joinPoint: ProceedingJoinPoint): Any? {
        var allowedExecute = false
        if (lock.tryLock()) {
            allowedExecute = true
        } else {
            lock.lock()
        }
        try {
            if (allowedExecute) {
                joinPoint.proceed()
            }
        } finally {
            lock.unlock()
        }
        return null // TODO: Return value from the single execution
    }
}

class Pointcuts {
    @Pointcut("@target(Exclusive)")
    fun beanAnnotated() {
    }

    @Pointcut("@annotation(Exclusive)")
    fun methodAnnotated() {
    }

    @Pointcut("execution(public * io.averkhoglyad.proto..*.*(..))")
    fun anyPublicMethodExecution() {
    }

    @Pointcut("(beanAnnotated() || methodAnnotated()) && anyPublicMethodExecution()")
    fun exclusiveExecution() {
    }
}
