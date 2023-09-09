package io.averkhoglyad.proto.example;

public class InheritedComponent extends SomeComponent {

    public void test3(Object arg1, Object arg2, Object arg3) {
        waitAndThen(() -> System.out.println("test3(" + arg1 + ", " + arg2 + ", " + arg3 + ")"));
    }

}
