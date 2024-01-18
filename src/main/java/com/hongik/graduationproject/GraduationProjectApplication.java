package com.hongik.graduationproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class GraduationProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraduationProjectApplication.class, args);
    }

}
