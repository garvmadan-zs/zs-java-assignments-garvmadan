package com.zs.assignment9.service;

import com.zs.assignment7.model.Student;
import com.zs.assignment9.DAO.StudentsDao;
import com.zs.assignment9.exception.InvalidNameException;
import com.zs.assignment9.exception.StudentNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentQueryServiceTest {

    private StudentsDao studentDAO;
    private StudentQueryService studentQueryService;

    @BeforeEach
    void setUp() {
        studentDAO = mock(StudentsDao.class);
        studentQueryService = new StudentQueryService(studentDAO);
    }

    @Test
    void createStudent_shouldCreateStudentSuccessfully() throws InvalidNameException {

        Student student = studentQueryService.createStudent("John", "Doe");

        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertNotNull(student.getId());

        verify(studentDAO).insertStudents(student);
        verifyNoMoreInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldGenerateUniqueIds() throws InvalidNameException {

        Student student1 = studentQueryService.createStudent("John", "Doe");
        Student student2 = studentQueryService.createStudent("John", "Doe");

        assertNotEquals(student1.getId(), student2.getId());

        verify(studentDAO, times(2)).insertStudents(any(Student.class));
        verifyNoMoreInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldGenerateCorrectPrefixes() throws InvalidNameException {

        studentQueryService.createStudent("John", "Doe");

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

        verify(studentDAO).insertStudents(captor.capture());

        Student captured = captor.getValue();

        assertTrue(captured.getId().startsWith("JODO"));
    }

    @Test
    void createStudent_shouldPadSingleCharacterNames() throws InvalidNameException {

        Student student = studentQueryService.createStudent("A", "B");

        assertTrue(student.getId().startsWith("AXBX"));
    }

    @Test
    void createStudent_shouldRejectNullFirstName() {

        assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent(null, "Doe")
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectNullLastName() {

        assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John", null)
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectBlankFirstName() {

        assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("   ", "Doe")
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectBlankLastName() {

        assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John", " ")
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectInvalidFirstName() {

        assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John123", "Doe")
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectInvalidLastName() {

        assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John", "Doe@123")
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void getStudent_shouldReturnStudent() throws StudentNotFound {

        Student expected =
                new Student("JODO010000001234", "John", "Doe", null);

        when(studentDAO.getStudent(expected.getId()))
                .thenReturn(expected);

        Student actual =
                studentQueryService.getStudent(expected.getId());

        assertSame(expected, actual);

        verify(studentDAO).getStudent(expected.getId());
        verifyNoMoreInteractions(studentDAO);
    }

    @Test
    void getStudent_shouldThrowStudentNotFound() throws StudentNotFound {

        when(studentDAO.getStudent("INVALID"))
                .thenThrow(new StudentNotFound("Student not found"));

        assertThrows(
                StudentNotFound.class,
                () -> studentQueryService.getStudent("INVALID")
        );

        verify(studentDAO).getStudent("INVALID");
        verifyNoMoreInteractions(studentDAO);
    }

    @Test
    void getStudent_shouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> studentQueryService.getStudent(null)
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void getStudent_shouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> studentQueryService.getStudent(" ")
        );

        verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldInsertExactlyCapturedStudent() throws InvalidNameException {

        studentQueryService.createStudent("Alex", "Smith");

        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentDAO).insertStudents(captor.capture());

        Student captured = captor.getValue();

        assertEquals("Alex", captured.getFirstName());
        assertEquals("Smith", captured.getLastName());
        assertNotNull(captured.getId());
    }
}
