package com.bankmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankManagementSystemApplication {

    public static void main(String[] args) {
        System.setProperty("server.port", "8080");
        SpringApplication.run(BankManagementSystemApplication.class, args);
    }

}