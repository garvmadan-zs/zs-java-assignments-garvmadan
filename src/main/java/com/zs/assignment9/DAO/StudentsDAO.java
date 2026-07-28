package com.zs.assignment9.DAO;
import com.zs.assignment7.DAO.StudentDAO;
import com.zs.assignment7.model.Student;
import com.zs.assignment7.util.DataBaseCoonection;
import com.zs.assignment9.exception.StudentNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class StudentsDAO {
    private static final Logger logger =
            LoggerFactory.getLogger(StudentDAO.class);

    public void insertStudents(Student student) {
        String sql = """
                INSERT INTO students (id, first_name, last_name, mobile) VALUES (?,?,?,?)""";

        try (Connection connection = DataBaseCoonection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, student.getId());
                ps.setString(2, student.getFirstName());
                ps.setString(3, student.getLastName());
                ps.setString(4, student.getMobile());


            ps.executeUpdate();
            logger.info("Created and Inserted a student successfully");

        } catch (SQLException e) {
            logger.error("Error in inserting the batch", e);
        }
    }
   public Student getStudent(String studentId) throws StudentNotFound {
       String sql = """
                SELECT s.id, s.first_name, s.last_name, s.mobile
                FROM students s
                WHERE s.id= ? """;
       try (Connection connection = DataBaseCoonection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

           ps.setString(1, studentId);
           ResultSet rs=ps.executeQuery();
           if (rs.next()) {
               String id = rs.getString("id");
               String firstName = rs.getString("first_name");
               String lastName = rs.getString("last_name");
               String mobile = rs.getString("mobile");

               logger.info("Student Fetched Successfully");


               return new Student(id, firstName, lastName, mobile);
           }



       } catch (SQLException e) {
           logger.error("Error in inserting the batch", e);

       }
       throw new StudentNotFound("Student not found with ID: " + studentId);


   }




}
