package com.zs.assignment9.controller;
import com.zs.assignment4.controller.Menu;
import com.zs.assignment4.services.CatalogService;
import com.zs.assignment7.DAO.StudentDAO;
import com.zs.assignment9.DAO.StudentsDAO;
import com.zs.assignment9.exception.InvalidMobileException;
import com.zs.assignment9.service.StudentQueryService;
import com.zs.assignment9.exception.InvalidNameException;
import java.util.Scanner;

public class StudentQueryController {
    private final StudentQueryService studentQueryService;
    private final Scanner scanner;

    public StudentQueryController(Scanner scanner,StudentQueryService studentQueryService) {
        this.scanner = scanner;
        this.studentQueryService = studentQueryService;
    }
    public void getStudent(){
        System.out.println("Enter the id : ");
        String studentId = scanner.nextLine();
        studentQueryService.getStudent(studentId);
    }
    public void insertStudent(){
        System.out.println("Enter the first name of the student  : ");
        String firstName=scanner.nextLine();
        if (!firstName.matches("[A-Za-z ]+")) {
            throw new InvalidNameException("first name should contain only letters.");
        }
        System.out.println("Enter the last name of the student  : ");
        String lastName=scanner.nextLine();
        if (!lastName.matches("[A-Za-z ]+")) {
            throw new InvalidNameException("last name should contain only letters.");
        }
        System.out.println("Enter the Mobile number of the student : ");
        String mobile=scanner.nextLine();
        if (!mobile.matches("^\\d{10}$")) {
            throw new InvalidMobileException(
                    "Mobile number must contain exactly 10 digits."
            );
        }
        studentQueryService.createStudent(firstName,lastName,mobile);
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
                        break;
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
