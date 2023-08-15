package io.averkhoglyad.proto.exclusive

import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

open class ExclusiveExecutionConfig(
    @Value("\${exclusive-execution.package:}")
    private val targetPackage: String
) {
    @Bean
    fun exclusiveExecutionPointcut(): ExclusiveExecutionPointcut {
        return ExclusiveExecutionPointcut(targetPackage)
    }

    @Bean
    fun exclusiveExecutionMethodInterceptor(): ExclusiveExecutionMethodInterceptor {
        return ExclusiveExecutionMethodInterceptor()
    }

    @Bean
    fun defaultPointcutAdvisor(): DefaultPointcutAdvisor {
        return DefaultPointcutAdvisor(exclusiveExecutionPointcut(), exclusiveExecutionMethodInterceptor())
    }
}