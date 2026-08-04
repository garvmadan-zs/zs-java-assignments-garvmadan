package com.zs.assignment10.DAO;

import com.zs.assignment10.Exception.ProductNotFoundException;
import com.zs.assignment10.model.Product;
import org.postgresql.largeobject.BlobOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zs.assignment10.config.DBConnection;

import java.rmi.StubNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DaoImplementation implements DaoInterface{
    private static final Logger logger= LoggerFactory.getLogger(DaoImplementation.class);
    @Override
    public void createProductTable() {
        String  productTable = """
                CREATE TABLE IF NOT EXISTS products(
                 name VARCHAR(50) UNIQUE,
                 id INT PRIMARY KEY,
                 price FLOAT,
                 quantity INT);
                 """;
        try (Connection connection = DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(productTable)) {

            preparedStatement.execute();
            logger.info("Product table created.");

        } catch (Exception e) {
            logger.error("Error creating tables", e);
        }


    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String findAll= """
                SELECT name,id,price,quantity FROM products ;""";
        try(Connection connection =DBConnection.getDataSource().getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(findAll)){
            ResultSet resultSet=preparedStatement.executeQuery();

            while(resultSet.next()){
                Product product=new Product(resultSet.getString("name"),
                        resultSet.getInt("id"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("quantity"));
                products.add(product);
            }
            logger.info("Products fetched successfully!!!!");
            return products;
        }
        catch (Exception e){
            logger.error("Products cant be fetched",e);
        }
        return products;
        }

   @Override
    public Product findById(int id) throws ProductNotFoundException {
       Product product;
       String findById = """
               SELECT name,id,price,quantity FROM products WHERE id =?; """;
       try(Connection connection=DBConnection.getDataSource().getConnection();
       PreparedStatement statement=connection.prepareStatement(findById)){
           statement.setInt(1,id);
           ResultSet resultSet=statement.executeQuery();
           if(resultSet.next()){
               product=new Product(resultSet.getString("name"),resultSet.getInt("id"),resultSet.getDouble("price"),resultSet.getInt("quantity"));
                logger.info("Product fetched successfully");
                return product;
           }

       }
       catch (Exception e){
                logger.error("Cant find the product with given id",e);
           }
            throw new ProductNotFoundException("Product not found with the id :" + id);

    }

    @Override
    public void insertProduct(Product product) {
        String insertProduct ="""
                INSERT INTO products (name,id,price,quantity) 
                VALUES (?,?,?,?);
                """;
        try(Connection connection=DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertProduct)) {
            preparedStatement.setString(1,product.getName());
            preparedStatement.setInt(2,product.getId());
            preparedStatement.setDouble(3,product.getPrice());
            preparedStatement.setInt(4,product.getQuantity());

            preparedStatement.executeUpdate();
            logger.info("Product Inserted successfully ");
        }
        catch (Exception e){
                logger.error("Product Insertion failed ",e);
        }

    }
    @Override
    public void updatePrice (int id, double price){
            String updatePrice= """
                    UPDATE products 
                    SET price=?
                    WHERE id=?;
                    """;
            try(Connection connection=DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(updatePrice)){
                preparedStatement.setDouble(1,price);
                preparedStatement.setInt(2,id);
                preparedStatement.executeUpdate();
                logger.info("Price Updated Successfully");
            }
            catch (Exception e){
                logger.error("Price was not updated.",e);
            }

    }
    @Override
    public void updateName (int id,String name){
        String updatePrice= """
                    UPDATE products 
                    SET name=?
                    WHERE id=?;
                    """;
        try(Connection connection=DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(updatePrice)){
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,id);
            preparedStatement.executeUpdate();
            logger.info("Name Updated Successfully");
        }
        catch (Exception e){
            logger.error("Name was not updated.",e);
        }

    }
    @Override
    public void updateQuantity (int id,int quantity){
        String updatePrice= """
                    UPDATE products 
                    SET quantity=?
                    WHERE id=?;
                    """;
        try(Connection connection=DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(updatePrice)){
            preparedStatement.setInt(1,quantity);
            preparedStatement.setInt(2,id);
            preparedStatement.executeUpdate();
            logger.info("Quantity Updated Successfully");
        }
        catch (Exception e){
            logger.error("Quantity was not updated.",e);
        }

    }

    @Override
    public void deleteProduct(int id) {
       String deleteProduct = """
               DELETE FROM products WHERE id =?;
               """;
       try(Connection connection= DBConnection.getDataSource().getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(deleteProduct)){
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            logger.info("Product deleted successfully");
       }
       catch (Exception e){
           logger.error("Product cant be deleted ",e);
       }
    }

    @Override
    public boolean exists(int id) throws ProductNotFoundException {
          try(Product product=findById(id)) {
                logger.info("Product exists");
                return true;
          }
          catch (Exception e){
              logger.error("Error in finding the product");
          }
          return false;
    }
}
