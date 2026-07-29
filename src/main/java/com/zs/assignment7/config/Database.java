package com.zs.assignment7.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

public class Database {

    private static final HikariDataSource dataSource;

    static {
        Dotenv dotenv = Dotenv.configure().filename(".env.local").load();
        HikariConfig config = new HikariConfig();
        String host = dotenv.get("DB_HOST");
        String port = dotenv.get("DB_PORT");
        String db = dotenv.get("DB_NAME");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");


        config.setJdbcUrl(
                String.format("jdbc:postgresql://%s:%s/%s", host, port, db)
        );
        config.setUsername(user);
        config.setPassword(password);

        config.setMaximumPoolSize(
                Integer.parseInt(dotenv.get("HIKARI_MAX_POOL_SIZE", "10"))
        );
        config.setMinimumIdle(
                Integer.parseInt(dotenv.get("HIKARI_MIN_IDLE", "2"))
        );
        config.setConnectionTimeout(
                Long.parseLong(dotenv.get("HIKARI_CONNECTION_TIMEOUT", "30000"))
        );

        dataSource = new HikariDataSource(config);
    }

    public static javax.sql.DataSource getDataSource() {
        return dataSource;
    }
}