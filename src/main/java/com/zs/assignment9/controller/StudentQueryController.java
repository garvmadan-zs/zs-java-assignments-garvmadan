package com.zs.assignment9.controller;
import com.zs.assignment7.model.Student;
import com.zs.assignment9.exception.StudentNotFound;
import com.zs.assignment9.service.StudentQueryService;
import com.zs.assignment9.exception.InvalidNameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class StudentQueryController {
    private final StudentQueryService studentQueryService;
    private final Scanner scanner;
    private static final Logger logger =
            LoggerFactory.getLogger(StudentQueryController.class);


    public StudentQueryController(Scanner scanner,StudentQueryService studentQueryService) {
        this.scanner = scanner;
        this.studentQueryService = studentQueryService;
    }
    public void getStudent(){
        System.out.print("Enter student id : ");

        String studentId = scanner.nextLine();

        try {

            Student student =
                    studentQueryService.getStudent(studentId);

            System.out.println(student);

            logger.info("Student fetched successfully.");

        }
        catch (StudentNotFound e) {

            logger.error(e.getMessage());

        }

    }
    public void insertStudent() throws InvalidNameException{
        try {

            System.out.print("First Name : ");

            String firstName = scanner.nextLine();

            System.out.print("Last Name : ");

            String lastName = scanner.nextLine();

            Student student =
                    studentQueryService.createStudent(
                            firstName,
                            lastName);

            System.out.println(student);

            logger.info("Student inserted successfully.");

        }

        catch (InvalidNameException e) {

            logger.error(e.getMessage());

        }
    }
    public void showMenu() {
        StudentMenu menu= new StudentMenu();
        while (true) {

            menu.displayMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        getStudent();
                        break;
                    case "2":
                        insertStudent();
                        break;
                    case "3":
                        System.out.println("Exiting the Application");
                       return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("Unexpected error: " + ex.getMessage());
            }
        }
    }
}



