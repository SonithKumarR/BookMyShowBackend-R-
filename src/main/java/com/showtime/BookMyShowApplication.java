package com.showtime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookMyShowApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookMyShowApplication.class, args);
    }
}