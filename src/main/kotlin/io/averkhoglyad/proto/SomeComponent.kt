package io.averkhoglyad.proto

import io.averkhoglyad.proto.exclusive.Exclusive

@Exclusive
open class SomeComponent {
    open fun test0() {
        waitAndThen { println("test0()") }
    }

    open fun test1(arg1: Any) {
        waitAndThen { println("test1($arg1)") }
    }

    open fun test2(arg1: Any, arg2: Any) {
        waitAndThen { println("test2($arg1, $arg2)") }
    }

    open fun test1_2(@Exclusive.Key arg1: Any, arg2: Any) {
        waitAndThen { println("test1_2($arg1, $arg2)") }
    }

    private fun waitAndThen(fn: Runnable) {
        try {
            Thread.sleep(3000)
            fn.run()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}
