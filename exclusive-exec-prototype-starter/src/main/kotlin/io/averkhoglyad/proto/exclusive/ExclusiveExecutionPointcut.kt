package io.averkhoglyad.proto.exclusive

import org.springframework.aop.ClassFilter
import org.springframework.aop.support.DynamicMethodMatcherPointcut
import org.springframework.core.annotation.AnnotationUtils
import java.lang.reflect.Method
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.hasAnnotation

class ExclusiveExecutionPointcut(private val targetPackage: String) : DynamicMethodMatcherPointcut() {

    override fun matches(method: Method, targetClass: Class<*>, vararg args: Any?): Boolean {
        return targetClass.kotlin.hasAnnotation<Exclusive>() || method.declaredAnnotations.any { it is Exclusive }
    }

    override fun getClassFilter(): ClassFilter {
        return ClassFilter {
            if (it.isSynthetic) {
                false
            } else {
                isTargetPackage(it) && hasAnnotatedMethods(it)
            }
        }
    }

    private fun isTargetPackage(klass: Class<*>): Boolean {
        return targetPackage.isBlank() || klass.packageName.startsWith(targetPackage)
    }

    private fun hasAnnotatedMethods(klass: Class<*>): Boolean {
        return klass.kotlin.hasAnnotation<Exclusive>() || klass.kotlin.declaredFunctions.any { fn -> fn.hasAnnotation<Exclusive>() }
    }
}
