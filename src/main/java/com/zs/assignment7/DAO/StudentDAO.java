package com.zs.assignment7.DAO;

import com.zs.assignment7.config.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.zs.assignment7.model.Student;

import java.util.List;

public class StudentDAO {
    private static final Logger logger =
            LoggerFactory.getLogger(StudentDAO.class);

    public void createTables() {

        String studentTable = """
                CREATE TABLE IF NOT EXISTS students(
                    id VARCHAR(16) PRIMARY KEY,
                    first_name VARCHAR(50),
                    last_name VARCHAR(50),
                    mobile VARCHAR(15)
                );
                """;

        String departmentTable = """
                CREATE TABLE IF NOT EXISTS departments(
                    dept_id SERIAL PRIMARY KEY,
                    dept_name VARCHAR(20) UNIQUE
                );
                """;

        String mappingTable = """
                CREATE TABLE IF NOT EXISTS student_department(
                    student_id VARCHAR(16) REFERENCES students(id),
                    dept_id INT REFERENCES departments(dept_id),
                    PRIMARY KEY(student_id)
                );
                """;

        try (Connection connection = Database.getDataSource().getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(studentTable);
            logger.info("Students table created.");

            statement.execute(departmentTable);
            logger.info("Departments table created.");

            statement.execute(mappingTable);
            logger.info("Student_Department table created.");

        } catch (Exception e) {
            logger.error("Error creating tables", e);
        }
    }

    public void insertDepartments() {
        String sql = """
                INSERT INTO departments (dept_name)
                VALUES ('CS') , ('EE'), ('Mech')
                ON CONFLICT (dept_name) DO NOTHING""";
        try (Connection connection = Database.getDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Department names inserted ");
        } catch (Exception e) {
            logger.error("Error in inserting the departments ", e);
        }

    }

    public void insertStudents(List<Student> students) {
        String sql = """
                INSERT INTO students (id, first_name, last_name, mobile) VALUES (?,?,?,?)""";

        try (Connection connection = Database.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (Student student : students) {
                ps.setString(1, student.getId());
                ps.setString(2, student.getFirstName());
                ps.setString(3, student.getLastName());
                ps.setString(4, student.getMobile());
                ps.addBatch();
            }
            ps.executeBatch();
            connection.commit();
            ps.clearBatch();

        } catch (Exception e) {
            logger.error("Error in inserting the batch", e);
        }
    }

    public void assignDepartments() {
        String sql = """
                      INSERT INTO student_department (student_id, dept_id)
                       SELECT s.id,
                              (SELECT dept_id
                               FROM departments
                               ORDER BY random()
                               LIMIT 1):: INT
                       FROM students s
                       ON CONFLICT(student_id) DO NOTHING;
                """;
        try (Connection connection = Database.getDataSource().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            logger.info("Departments assigned to the students and inserted in the Mapping Table ");
        } catch (Exception e) {
            logger.error("Error in assigning the departments ", e);
        }
    }
}
