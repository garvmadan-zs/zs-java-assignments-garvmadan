package com.zs.assignment7.controller;

import com.zs.assignment7.service.StudentService;
import com.zs.assignment7.DAO.StudentDAO;

public class StudentController {
    private final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public void generateStudents(int totalStudents) {
        studentService.generateStudents(totalStudents);
    }

    public void assignDepartments() {
        studentService.assignDepartments();
    }


}
