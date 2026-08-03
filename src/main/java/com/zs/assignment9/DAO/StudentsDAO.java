package com.zs.assignment9.DAO;

import com.zs.assignment7.config.Database;
import com.zs.assignment7.model.Student;
import com.zs.assignment9.exception.DatabaseException;
import com.zs.assignment9.exception.StudentNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsDao implements StudentDaoInterface {

    private static final Logger logger =
            LoggerFactory.getLogger(StudentDaoInterface.class);

    public void insertStudents(Student student) {

        String sql = """
                INSERT INTO students 
                (id, first_name, last_name, mobile) 
                VALUES (?,?,?,?)
                """;

        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, student.getId());
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());
            ps.setString(4, student.getMobile());

            ps.executeUpdate();

            logger.info("Created and inserted student successfully");

        } catch (SQLException e) {

            logger.error("Database error while inserting student", e);

            throw new DatabaseException(
                    "Unable to insert student",
                    e
            );
        }
    }


    public Student getStudent(String studentId)
            throws StudentNotFound {

        String sql = """
                SELECT s.id, s.first_name, s.last_name, s.mobile
                FROM students s
                WHERE s.id = ?
                """;

        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, studentId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String id = rs.getString("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String mobile = rs.getString("mobile");

                logger.info("Student fetched successfully");

                return new Student(
                        id,
                        firstName,
                        lastName,
                        mobile
                );
            }

        } catch (SQLException e) {

            logger.error("Database error while fetching student", e);

            throw new DatabaseException(
                    "Unable to fetch student",
                    e
            );
        }
        throw new StudentNotFound(
                "Student not found with ID: " + studentId
        );
    }
}
