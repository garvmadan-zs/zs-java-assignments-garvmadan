package com.zs.assignment7.service;

import com.github.javafaker.Faker;
import com.zs.assignment7.DAO.StudentDAO;
import com.zs.assignment7.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
public class StudentService {
    private final StudentDAO studentDAO;

    private int batch_Size=5000;
    private static final Logger logger= LoggerFactory.getLogger(StudentService.class);
    private final Faker faker=new Faker();

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    private String getPrefix(String name){
        name =name.replaceAll("[^a-zA-Z]","");
        if(name.length()>=2){
            return name.substring(0,2).toUpperCase();
        }
        else if(name.length()==1){
            return (name+"X").toUpperCase();
        }
        return "XX";
    }

    private String generateMobile(){
        return String.valueOf(ThreadLocalRandom.current().nextLong(6000000000L,
                9999999999L));
    }


    private String generateStudentId(String firstName,String lastName, int seq){
        String first=getPrefix(firstName);
        String last=getPrefix(lastName);
        String number=String.format("%08d",seq);
        int random = ThreadLocalRandom.current().nextInt(
                                1000,
                                10000);
        return first + last + number + random;
    }
    public void generateStudents(int totalStudents){
        List<Student> batch=new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for(int i=1;i<=totalStudents;i++){
            String firstName=faker.name().firstName();
            String lastName=faker.name().lastName();
            Student student =new Student(
                    generateStudentId(firstName,lastName,i),
                    firstName,lastName,generateMobile());
            batch.add(student);
            if(batch.size()==batch_Size){
                studentDAO.insertStudents(batch);
                logger.info("{} students inserted.", i);

                batch.clear();
            }

        }
        if(!batch.isEmpty()){
            studentDAO.insertStudents(batch);
            batch.clear();
        }
        long endTime = System.currentTimeMillis();
        logger.info(
                "Completed in {} ms",
                endTime - startTime
        );




    }






    /*public void generateStudents(String fileName, int totalStudents){
        logger.info("Starting the data generation for students ");
        try(BufferedWriter writer =new BufferedWriter(new FileWriter(fileName))){
            for(int i=1;i<=totalStudents;i++){
                String firstName=faker.name().firstName();
                String lastName=faker.name().lastName();
                Student student =new Student(generateStudentId(firstName,lastName,i),firstName,lastName,generateMobile());
                writer.write(student.getId()+","+student.getFirstName()+","+student.getLastName()+","+student.getMobile());
                writer.newLine();
                if(i%1000==0){
                    logger.info("100000 Records Generated ");
                }
            }
            logger.info("All the records generated successfully");
        }


        catch (IOException e){
            logger.error("Error while generation the CSV file");

        }
    }*/
    public  void assignDepartments(){
        studentDAO.assignDepartments();
    }



}


