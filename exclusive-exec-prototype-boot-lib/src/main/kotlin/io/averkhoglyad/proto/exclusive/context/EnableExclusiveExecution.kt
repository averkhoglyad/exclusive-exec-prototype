package io.averkhoglyad.proto.exclusive.context

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(ExclusiveExecutionConfig::class)
annotation class EnableExclusiveExecution
