package com.zs.assignment10.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConnection {

    private static final HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env.local")
                .load();

        Properties props = new Properties();
        try (InputStream input = DBConnection.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new RuntimeException("db.properties not found");
            }

            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load db.properties", e);
        }

        HikariConfig config = new HikariConfig();

        String host = props.getProperty("DB_HOST");
        String port = props.getProperty("DB_PORT");
        String db = props.getProperty("DB_NAME");

        config.setJdbcUrl(
                String.format("jdbc:postgresql://%s:%s/%s", host, port, db)
        );

        // Credentials from .env.local
        config.setUsername(dotenv.get("DB_USER"));
        config.setPassword(dotenv.get("DB_PASSWORD"));

        // Hikari settings from db.properties
        config.setMaximumPoolSize(
                Integer.parseInt(props.getProperty("HIKARI_MAX_POOL_SIZE", "10"))
        );
        config.setMinimumIdle(
                Integer.parseInt(props.getProperty("HIKARI_MIN_IDLE", "2"))
        );
        config.setConnectionTimeout(
                Long.parseLong(props.getProperty("HIKARI_CONNECTION_TIMEOUT", "30000"))
        );

        dataSource = new HikariDataSource(config);
    }

    public static javax.sql.DataSource getDataSource() {
        return dataSource;
    }
}