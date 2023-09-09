package io.averkhoglyad.proto.example;

import io.averkhoglyad.proto.exclusive.context.EnableExclusiveExecution;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.IntStream;

@SpringBootApplication
@EnableExclusiveExecution
public class ProtoApplication {

    @Bean
    InheritedComponent someComponent() {
        return new InheritedComponent();
    }

    public static void main(String[] args) {
        var context = SpringApplication.run(ProtoApplication.class, args);

        var bean = context.getBean(InheritedComponent.class);

        doInParallel(() -> bean.test0());
        doInParallel(() -> bean.test1_2(1, 2));
        doInParallel(() -> bean.test1_2(1, 2));
        doInParallel(() -> bean.test1_2(1, 1));
        doInParallel(() -> bean.test1_2(1, -1));
        doInParallel(() -> bean.test1_2(2, 2));
        doInParallel(() -> bean.test1_2(2, 2));
        doInParallel(() -> bean.test1_2(2, 2));

        doInParallel(() -> bean.test3(1, 2, 3));
        doInParallel(() -> bean.test3(1, 2, 3));
        doInParallel(() -> bean.test3(1, 2, 3));

        IntStream.range(0, 3)
                .forEach(i -> doInParallel(() -> bean.test1_2(1, i)));
    }

    private static void doInParallel(Runnable fn) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            fn.run();
        }).start();
    }
}

