package org.example.aces_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AcesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcesApiApplication.class, args);
    }

}
