package io.averkhoglyad.proto.example;

import io.averkhoglyad.proto.exclusive.Exclusive;

public class SomeComponent {

    @Exclusive
    public void test0() {
        waitAndThen(() -> System.out.println("test0()"));
    }

    @Exclusive
    public void test1(Object arg1) {
        waitAndThen(() -> System.out.println("test1(" + arg1 + ")"));
    }

    @Exclusive
    public void test2(Object arg1, Object  arg2) {
        waitAndThen(() -> System.out.println("test2(" + arg1 + ", " + arg2 + ")"));
    }

    @Exclusive
    public void test1_2(@Exclusive.Key Object arg1, Object arg2) {
        waitAndThen(() -> System.out.println("test1_2(" + arg1 + ", " + arg2 + ")"));
    }

    protected void waitAndThen(Runnable fn) {
        try {
            Thread.sleep(3000);
            fn.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
