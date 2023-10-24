package io.averkhoglyad.proto.exclusive.context

import io.averkhoglyad.proto.exclusive.impl.ExclusiveExecutionMethodInterceptor
import io.averkhoglyad.proto.exclusive.impl.ExclusiveExecutionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

open class ExclusiveExecutionConfig {
    @Bean
    fun defaultPointcutAdvisor(@Value("\${exclusive-execution.package:}") targetPackage: String): DefaultPointcutAdvisor {
        return DefaultPointcutAdvisor(ExclusiveExecutionPointcut(targetPackage), ExclusiveExecutionMethodInterceptor())
    }
}