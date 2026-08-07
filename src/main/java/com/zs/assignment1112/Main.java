package com.zs.assignment1112;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().filename(".env.local").ignoreIfMissing().load();
        setPropertyIfPresent(dotenv, "DB_HOST");
        setPropertyIfPresent(dotenv, "DB_NAME");
        setPropertyIfPresent(dotenv, "DB_PORT");
        setPropertyIfPresent(dotenv, "DB_USER");
        setPropertyIfPresent(dotenv, "DB_PASSWORD");
        SpringApplication.run(Main.class, args);
    }

    private static void setPropertyIfPresent(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value != null && !value.isBlank()) {
            System.setProperty(key, value);
        }
    }
}
