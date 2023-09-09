package io.averkhoglyad.proto.exclusive.impl

import io.averkhoglyad.proto.exclusive.annotation.Exclusive
import org.springframework.aop.ClassFilter
import org.springframework.aop.support.DynamicMethodMatcherPointcut
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaMethod

class ExclusiveExecutionPointcut(private val targetPackage: String) : DynamicMethodMatcherPointcut() {

    override fun matches(method: Method, targetClass: Class<*>, vararg args: Any?): Boolean {
        return AnnotationUtils.findAnnotation(targetClass, Exclusive::class.java) != null
                || AnnotationUtils.findAnnotation(method, Exclusive::class.java) != null
    }

    override fun getClassFilter(): ClassFilter {
        return ClassFilter {
            if (it.isSynthetic) {
                false
            } else {
                it.isInTargetPackage() && it.hasAnnotatedMethods()
            }
        }
    }

    private fun Class<*>.isInTargetPackage(): Boolean {
        return targetPackage.isBlank() || packageName.startsWith(targetPackage)
    }

    private fun Class<*>.hasAnnotatedMethods(): Boolean {
        return AnnotationUtils.findAnnotation(this, Exclusive::class.java) != null
                || this.kotlin.functions.any { fn -> AnnotationUtils.findAnnotation(fn.javaMethod!!, Exclusive::class.java) != null }
    }
}
