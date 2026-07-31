package com.zs.assignment7;

import com.zs.assignment7.DAO.ExportDAO;
import com.zs.assignment7.controller.ExportController;
import com.zs.assignment7.controller.StudentController;
import com.zs.assignment7.service.StudentExportService;
import com.zs.assignment7.service.StudentService;
import com.zs.assignment7.util.CompressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zs.assignment7.DAO.StudentDAO;

public class Main {
    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        ExportDAO exportDAO = new ExportDAO();
        StudentExportService exportService = new StudentExportService(exportDAO);
        ExportController exportController = new ExportController(exportService);
        logger.info("Application Started");
        StudentDAO dao = new StudentDAO();
        dao.createTables();
        dao.insertDepartments();
        StudentDAO studentDAO = new StudentDAO();
        StudentService generator = new StudentService(studentDAO);
        StudentController studentController = new StudentController(generator);
        studentController.generateStudents(1_000_000);
        studentController.assignDepartments();
        exportController.exportStudents("student_departments.csv");
        CompressUtil.compressFile("student_departments.csv", "student_departments_compressed.csv.gz");
    }
}
