package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TextAnalyserApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(TextAnalyserApplication.class);
        springApplication.run(args);
    }
}
