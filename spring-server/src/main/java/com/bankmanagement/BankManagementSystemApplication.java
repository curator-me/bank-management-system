package com.bankmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BankManagementSystemApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("server.port", dotenv.get("SERVER_PORT", "8080"));
        SpringApplication.run(BankManagementSystemApplication.class, args);
    }

}