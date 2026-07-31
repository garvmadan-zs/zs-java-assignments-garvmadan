package com.zs.assignment9.DAO;

import com.zs.assignment7.model.Student;
import com.zs.assignment9.exception.StudentNotFound;

public interface StudentDaoInterface {

    void insertStudents(Student student);

    Student getStudent(String id) throws StudentNotFound;
}

