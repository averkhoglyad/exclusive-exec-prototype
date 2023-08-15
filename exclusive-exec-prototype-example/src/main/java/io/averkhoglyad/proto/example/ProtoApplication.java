package io.averkhoglyad.proto.example;

import io.averkhoglyad.proto.exclusive.ExclusiveExecutionConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.stream.IntStream;

@SpringBootApplication
@Import(ExclusiveExecutionConfig.class)
public class ProtoApplication {

    @Bean
    SomeComponent someComponent() {
        return new SomeComponent();
    }

    public static void main(String[] args) {
        var context = SpringApplication.run(ProtoApplication.class, args);

        var bean = context.getBean(SomeComponent.class);

        doInParallel(() -> bean.test0());
        doInParallel(() -> bean.test1_2(1, 2));
        doInParallel(() -> bean.test1_2(1, 2));
        doInParallel(() -> bean.test1_2(1, 1));
        doInParallel(() -> bean.test1_2(1, -1));
        doInParallel(() -> bean.test1_2(2, 2));
        doInParallel(() -> bean.test1_2(2, 2));
        doInParallel(() -> bean.test1_2(2, 2));

        IntStream.range(0, 3)
                .parallel()
                .forEach(i -> bean.test1_2(1, i));
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

