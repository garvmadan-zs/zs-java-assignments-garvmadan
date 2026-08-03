package com.zs.assignment7.controller;

import com.zs.assignment7.service.StudentExportService;

public class ExportController {
    private final StudentExportService studentExportService;

    public ExportController(StudentExportService studentExportService) {
        this.studentExportService = studentExportService;
    }

    public void exportStudents(String filepath) {
        studentExportService.exportStudents(filepath);
    }
}
