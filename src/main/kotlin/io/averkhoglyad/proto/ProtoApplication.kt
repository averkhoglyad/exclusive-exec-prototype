package io.averkhoglyad.proto

import io.averkhoglyad.proto.exclusive.ExclusiveExecutionAspect
import io.averkhoglyad.proto.exclusive.ExclusiveExecutionMethodInterceptor
import io.averkhoglyad.proto.exclusive.ExclusiveExecutionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.BeanFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.Bean
import java.util.stream.IntStream
import kotlin.reflect.KClass

@SpringBootApplication
class ProtoApplication {

//    @Bean
//    fun exclusiveExecutionAspect(): ExclusiveExecutionAspect {
//        return ExclusiveExecutionAspect()
//    }

    @Bean
    fun exclusiveExecutionPointcut(): ExclusiveExecutionPointcut {
        return ExclusiveExecutionPointcut("io.averkhoglyad.proto")
    }

    @Bean
    fun exclusiveExecutionMethodInterceptor(): ExclusiveExecutionMethodInterceptor {
        return ExclusiveExecutionMethodInterceptor()
    }

    @Bean
    fun defaultPointcutAdvisor(): DefaultPointcutAdvisor {
        return DefaultPointcutAdvisor(exclusiveExecutionPointcut(), exclusiveExecutionMethodInterceptor())
    }

    @Bean
    fun someComponent(): SomeComponent {
        return SomeComponent()
    }
}

fun main(vararg args: String) {
    val context = runApplication<ProtoApplication>(*args)

    val bean = context.getBean<SomeComponent>()

    doInParallel { bean.test0() }
    doInParallel { bean.test1_2(1, 2) }
    doInParallel { bean.test1_2(1, 2) }
    doInParallel { bean.test1_2(2, 2) }

    IntStream.range(0, 3)
        .parallel()
        .forEach { _ -> bean.test1_2(1, 2) }
}

private fun doInParallel(fn: () -> Unit) {
    Thread {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
        fn()
    }.start()
}

inline fun <reified T : Any> runApplication(vararg args: String): ConfigurableApplicationContext = SpringApplication.run(T::class.java, *args)

inline fun <reified E: Any> BeanFactory.getBean() = getBean(E::class)

inline fun <E : Any> BeanFactory.getBean(klass: KClass<E>) = getBean(klass.java)
