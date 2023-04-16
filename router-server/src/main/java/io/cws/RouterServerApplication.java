package io.cws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableKafka
public class RouterServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouterServerApplication.class, args);
    }

}
