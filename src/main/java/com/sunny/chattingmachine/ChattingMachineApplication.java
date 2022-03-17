package com.sunny.chattingmachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ChattingMachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChattingMachineApplication.class, args);
    }

}
