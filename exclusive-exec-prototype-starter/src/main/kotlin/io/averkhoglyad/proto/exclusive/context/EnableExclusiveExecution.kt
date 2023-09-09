package io.averkhoglyad.proto.exclusive.context

import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@EnableAspectJAutoProxy(proxyTargetClass = false)
@Import(ExclusiveExecutionConfig::class)
annotation class EnableExclusiveExecution
