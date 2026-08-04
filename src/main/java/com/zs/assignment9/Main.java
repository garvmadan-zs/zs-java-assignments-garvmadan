package com.zs.assignment9;

import com.zs.assignment9.DAO.StudentsDao;
import com.zs.assignment9.controller.StudentQueryController;
import com.zs.assignment9.service.StudentQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zs.assignment9.controller.Initializer;

import java.util.Scanner;

public class Main {
    private static final Logger logger =
            LoggerFactory.getLogger(com.zs.assignment7.Main.class);

    public static void main(String[] args) {
        Initializer.initiate();
        Scanner scanner = new Scanner(System.in);
        StudentsDao studentsDAO=new StudentsDao();
        StudentQueryService studentQueryService=new StudentQueryService(studentsDAO);
        StudentQueryController studentQueryController=new StudentQueryController(scanner,studentQueryService);
        studentQueryController.showMenu();
    }
}

