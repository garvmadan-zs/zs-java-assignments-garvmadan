package com.zs.assignment7.service;
import com.zs.assignment7.DAO.ExportDAO;

public class StudentExportService {
    private final ExportDAO exportDAO;
    public StudentExportService(ExportDAO exportDAO){
        this.exportDAO=exportDAO;
    }
    public void exportStudents(String filePath){
        exportDAO.exportStudents(filePath);
    }
}
