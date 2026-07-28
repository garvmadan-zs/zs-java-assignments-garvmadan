package com.zs.assignment9.service;
import com.zs.assignment7.model.Student;
import com.zs.assignment9.DAO.StudentsDAO;
import com.zs.assignment9.exception.StudentNotFound;

import java.util.concurrent.ThreadLocalRandom;

public class StudentQueryService {
    private final StudentsDAO studentDAO;
    final int sequence=1000000;
    public StudentQueryService(StudentsDAO studentDAO) {
        this.studentDAO = studentDAO;

    }
    private String getPrefix(String name) {
        name = name.replaceAll("[^a-zA-Z]", "");
        if (name.length() >= 2) {
            return name.substring(0, 2).toUpperCase();
        } else if (name.length() == 1) {
            return (name + "X").toUpperCase();
        }
        return "XX";
    }

    private String generateStudentId(String firstName, String lastName, int sequence) {
        String first = getPrefix(firstName);
        String last = getPrefix(lastName);
        String number = String.format("%08d", sequence);
        int random = ThreadLocalRandom.current().nextInt(
                1000,
                10000);
        sequence++;
        System.out.println("The generated id is : " + first+last+number+random);
        return first + last + number + random;

    }

    public void createStudent(String first_name, String last_name,String mobile){
        Student student =new Student(generateStudentId(first_name,last_name,sequence),first_name,last_name,mobile);
        studentDAO.insertStudents(student);
    }
    public void getStudent(String id){
        try{
            Student student=studentDAO.getStudent(id);
            System.out.println(student);

        }
        catch(StudentNotFound e){
            System.out.println(e);
        }

    }

}
