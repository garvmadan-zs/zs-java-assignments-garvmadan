package com.zs.assignment9.service;

import com.zs.assignment7.model.Student;
import com.zs.assignment9.DAO.StudentDaoInterface;
import com.zs.assignment9.exception.InvalidNameException;
import com.zs.assignment9.exception.StudentNotFoundException;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentQueryService {

    private final StudentDaoInterface studentDAO;

    private final AtomicInteger sequence =
            new AtomicInteger(1000000);

    public StudentQueryService(StudentDaoInterface studentDAO) {
        this.studentDAO = studentDAO;
    }

    private void validateName(String name, String fieldName)
            throws InvalidNameException {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException(
                    fieldName + " cannot be empty");
        }

        if (!name.matches("[A-Za-z ]+")) {
            throw new InvalidNameException(
                    fieldName + " should contain only letters");
        }
    }

    private String getPrefix(String name) {

        name = name.replaceAll("[^A-Za-z]", "");

        if (name.length() >= 2) {
            return name.substring(0, 2).toUpperCase();
        }

        if (name.length() == 1) {
            return (name + "X").toUpperCase();
        }

        return "XX";
    }

    private String generateStudentId(
            String firstName,
            String lastName) {

        String first = getPrefix(firstName);

        String last = getPrefix(lastName);

        String number = String.format(
                "%08d",
                sequence.getAndIncrement());

        int random =
                ThreadLocalRandom.current()
                        .nextInt(1000, 10000);

        return first + last + number + random;
    }

    public Student createStudent(
            String firstName,
            String lastName)
            throws InvalidNameException {

        validateName(firstName, "First name");
        validateName(lastName, "Last name");

        Student student =
                new Student(
                        generateStudentId(firstName, lastName),
                        firstName,
                        lastName,
                        null
                );

        studentDAO.insertStudents(student);

        return student;
    }

    public Student getStudent(String id)
            throws StudentNotFoundException {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Student id cannot be empty");
        }

        return studentDAO.getStudent(id);
    }
}
