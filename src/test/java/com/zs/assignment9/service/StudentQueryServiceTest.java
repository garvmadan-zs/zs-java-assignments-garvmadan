package com.zs.assignment9.service;


import com.zs.assignment7.model.Student;
import com.zs.assignment9.DAO.StudentsDAO;
import com.zs.assignment9.exception.StudentNotFound;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StudentQueryServiceTest {


    @Mock
    private StudentsDAO studentDAO;


    @InjectMocks
    private StudentQueryService studentService;


    @Test
    void createStudent_shouldInsertStudentSuccessfully() {


        studentService.createStudent(
                "John",
                "Doe",
                "9876543210"
        );


        verify(studentDAO, times(1))
                .insertStudents(any(Student.class));

    }


    @Test
    void createStudent_shouldStoreCorrectStudentDetails() {


        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);


        studentService.createStudent(
                "John",
                "Doe",
                "9876543210"
        );


        verify(studentDAO)
                .insertStudents(captor.capture());


        Student student =
                captor.getValue();


        assertEquals(
                "John",
                student.getFirstName()
        );


        assertEquals(
                "Doe",
                student.getLastName()
        );


        assertEquals(
                "9876543210",
                student.getMobile()
        );

    }


    @Test
    void createStudent_shouldGenerateStudentId() {


        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);


        studentService.createStudent(
                "John",
                "Doe",
                "9876543210"
        );


        verify(studentDAO)
                .insertStudents(captor.capture());


        Student student =
                captor.getValue();


        assertNotNull(
                student.getId()
        );


    }


    @Test
    void createStudent_shouldHandleSpecialCharactersInName() {


        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);


        studentService.createStudent(
                "J@ohn",
                "D#oe",
                "9876543210"
        );


        verify(studentDAO)
                .insertStudents(captor.capture());


        Student student =
                captor.getValue();


        assertTrue(
                student.getId()
                        .startsWith("JODO")
        );

    }


    @Test
    void createStudent_shouldHandleSingleCharacterName() {


        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);


        studentService.createStudent(
                "A",
                "B",
                "9876543210"
        );


        verify(studentDAO)
                .insertStudents(captor.capture());


        Student student =
                captor.getValue();


        assertTrue(
                student.getId()
                        .startsWith("AXBX")
        );

    }


    @Test
    void createStudent_shouldHandleEmptyName() {


        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);


        studentService.createStudent(
                "",
                "",
                "9876543210"
        );


        verify(studentDAO)
                .insertStudents(captor.capture());


        Student student =
                captor.getValue();


        assertTrue(
                student.getId()
                        .startsWith("XXXX")
        );

    }


    @Test
    void createStudent_shouldHandleDaoFailure() {


        doThrow(new RuntimeException())
                .when(studentDAO)
                .insertStudents(any(Student.class));


        assertThrows(
                RuntimeException.class,
                () ->
                        studentService.createStudent(
                                "John",
                                "Doe",
                                "9876543210"
                        )
        );

    }


    @Test
    void getStudent_shouldFindStudentSuccessfully()
            throws StudentNotFound {


        Student student =
                new Student(
                        "JD123456",
                        "John",
                        "Doe",
                        "9876543210"
                );


        when(studentDAO.getStudent("JD123456"))
                .thenReturn(student);


        studentService.getStudent(
                "JD123456"
        );


        verify(studentDAO)
                .getStudent("JD123456");

    }


    @Test
    void getStudent_shouldHandleStudentNotFound()
            throws StudentNotFound {


        when(studentDAO.getStudent("INVALID"))
                .thenThrow(
                        new StudentNotFound(
                                "Student not found"
                        )
                );


        assertDoesNotThrow(
                () ->
                        studentService.getStudent(
                                "INVALID"
                        )
        );


        verify(studentDAO)
                .getStudent("INVALID");

    }


    @Test
    void getStudent_shouldPassCorrectIdToDao()
            throws StudentNotFound {


        studentService.getStudent(
                "ABC123"
        );


        verify(studentDAO)
                .getStudent("ABC123");

    }


    @Test
    void getStudent_shouldHandleNullId()
            throws StudentNotFound {


        assertDoesNotThrow(
                () ->
                        studentService.getStudent(null)
        );


        verify(studentDAO)
                .getStudent(null);

    }


    @Test
    void getStudent_shouldHandleEmptyId()
            throws StudentNotFound {


        assertDoesNotThrow(
                () ->
                        studentService.getStudent("")
        );


        verify(studentDAO)
                .getStudent("");

    }


    @Test
    void getStudent_shouldPropagateUnexpectedException()
            throws StudentNotFound {


        when(studentDAO.getStudent("ABC"))
                .thenThrow(
                        new RuntimeException()
                );


        assertThrows(
                RuntimeException.class,
                () ->
                        studentService.getStudent("ABC")
        );

    }

}
