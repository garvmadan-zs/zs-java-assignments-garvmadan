package com.zs.assignment10.config;

import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import org.junit.jupiter.api.Assertions;

class DBConnectionTest {
    @Test
    void shouldCreateDataSource() {
        DataSource dataSource = DBConnection.getDataSource();
        Assertions.assertNotNull(dataSource);
    }
    @Test
    void shouldGetDatabaseConnection() throws Exception {
        DataSource dataSource = DBConnection.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Assertions.assertNotNull(connection);
            Assertions.assertFalse(connection.isClosed());}}
    @Test
    void shouldHaveValidDatabaseConnection() throws Exception {
        try(Connection connection = DBConnection.getDataSource().getConnection()) {
            boolean valid = connection.isValid(5);
            Assertions.assertTrue(valid);}
    }
}
