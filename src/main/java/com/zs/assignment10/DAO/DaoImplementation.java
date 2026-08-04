package com.zs.assignment10.DAO;

import com.zs.assignment10.Exception.*;
import com.zs.assignment10.config.DBConnection;
import com.zs.assignment10.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoImplementation implements DaoInterface {

    private static final Logger logger = LoggerFactory.getLogger(DaoImplementation.class);

    @Override
    public void createProductTable() {
        String productTable = """
                CREATE TABLE IF NOT EXISTS products(
                    name VARCHAR(50) UNIQUE,
                    id INT PRIMARY KEY,
                    price DOUBLE PRECISION,
                    quantity INT
                );
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(productTable)) {

            preparedStatement.execute();
            logger.info("Product table created successfully.");

        } catch (SQLException e) {
            logger.error("Error creating product table.", e);
            throw new RuntimeException("Unable to create product table.", e);
        }
    }

    @Override
    public List<Product> findAll() {

        List<Product> products = new ArrayList<>();

        String findAll = """
                SELECT name,id,price,quantity
                FROM products;
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findAll);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                products.add(new Product(
                        resultSet.getString("name"),
                        resultSet.getInt("id"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity")
                ));
            }

            logger.info("Products fetched successfully.");
            return products;

        } catch (SQLException e) {
            logger.error("Unable to fetch products.", e);
            throw new RuntimeException("Database error while fetching products.", e);
        }
    }

    @Override
    public Product findById(int id) throws ProductNotFoundException {

        String findById = """
                SELECT name,id,price,quantity
                FROM products
                WHERE id = ?;
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findById)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info("Product fetched successfully.");

                    return new Product(
                            resultSet.getString("name"),
                            resultSet.getInt("id"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("quantity")
                    );
                }
            }

            throw new ProductNotFoundException("Product not found with id: " + id);

        } catch (SQLException e) {
            logger.error("Database error while fetching product with id {}", id, e);
            throw new RuntimeException("Database error while fetching product.", e);
        }
    }

    @Override
    public void insertProduct(Product product) {

        String insertProduct = """
                INSERT INTO products(name,id,price,quantity)
                VALUES(?,?,?,?);
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProduct)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getId());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getQuantity());

            preparedStatement.executeUpdate();

            logger.info("Product inserted successfully.");

        } catch (SQLException e) {
            logger.error("Product insertion failed.", e);
            throw new RuntimeException("Unable to insert product.", e);
        }
    }

    @Override
    public void updateProduct(int id, String name,double price,int quantity) throws ProductNotFoundException {

        String updateName = """
                UPDATE products
                SET name = ?,
                    price=?,
                    quantity=?
                WHERE id = ?;
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateName)) {

            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2,price);
            preparedStatement.setInt(3,quantity);
            preparedStatement.setInt(4,id);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new ProductNotFoundException("Product not found with id: " + id);
            }

            logger.info("Product updated successfully.");

        } catch (SQLException e) {
            logger.error("Database error while updating.", e);
            throw new RuntimeException("Unable to update.", e);
        }
    }


    @Override
    public void deleteProduct(int id) throws ProductNotFoundException {

        String deleteProduct = """
                DELETE FROM products
                WHERE id = ?;
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteProduct)) {

            preparedStatement.setInt(1, id);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new ProductNotFoundException("Product not found with id: " + id);
            }

            logger.info("Product deleted successfully.");

        } catch (SQLException e) {
            logger.error("Database error while deleting product.", e);
            throw new RuntimeException("Unable to delete product.", e);
        }
    }

    @Override
    public boolean exists(int id) {

        String existsQuery = """
                SELECT 1
                FROM products
                WHERE id = ?;
                """;

        try (Connection connection = DBConnection.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(existsQuery)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                boolean exists = resultSet.next();

                logger.info("Product existence check for id {} : {}", id, exists);

                return exists;
            }

        } catch (SQLException e) {
            logger.error("Database error while checking product existence.", e);
            throw new DatabaseException("Unable to check product existence.", e);
        }
    }
}