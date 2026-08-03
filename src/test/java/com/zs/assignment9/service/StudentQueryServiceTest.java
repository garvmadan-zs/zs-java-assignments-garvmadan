package com.zs.assignment9.service;

import com.zs.assignment7.model.Student;
import com.zs.assignment9.DAO.StudentDaoInterface;
import com.zs.assignment9.exception.DatabaseException;
import com.zs.assignment9.exception.InvalidNameException;
import com.zs.assignment9.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentQueryServiceTest {
    private static StudentQueryService studentQueryService;
    @Mock
    private static StudentDaoInterface studentDAO;


    @BeforeAll
    static void setUp() {
        studentDAO = mock(StudentDaoInterface.class);
        studentQueryService = new StudentQueryService(studentDAO);
    }
    @BeforeEach
    void resetMocks() {
        Mockito.reset(studentDAO);
    }
    @Test
    void createStudent_shouldCreateStudentSuccessfully() {
        try {
            Student student = studentQueryService.createStudent("John", "Doe");

            assertNotNull(student);
            assertEquals("John", student.getFirstName());
            assertEquals("Doe", student.getLastName());
            assertNotNull(student.getId());

            verify(studentDAO).insertStudents(student);
            verifyNoMoreInteractions(studentDAO);
        } catch (InvalidNameException e) {
            fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldGenerateUniqueIds() {
        try {
            Student student1 = studentQueryService.createStudent("John", "Doe");
            Student student2 = studentQueryService.createStudent("John", "Doe");

            assertNotEquals(student1.getId(), student2.getId());

            verify(studentDAO, times(2)).insertStudents(any(Student.class));
            verifyNoMoreInteractions(studentDAO);
        } catch (InvalidNameException e) {
            fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldGenerateCorrectPrefixes() {
        try {
            studentQueryService.createStudent("John", "Doe");

            ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

            verify(studentDAO).insertStudents(captor.capture());

            Student captured = captor.getValue();

            assertTrue(captured.getId().startsWith("JODO"));
        } catch (InvalidNameException e) {
            fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldPadSingleCharacterNames() {
        try {
            Student student = studentQueryService.createStudent("A", "B");

            assertTrue(student.getId().startsWith("AXBX"));
        } catch (InvalidNameException e) {
            fail("Unexpected InvalidNameException: " + e.getMessage());
        }
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
    void getStudent_shouldReturnStudent() {
        try {
            Student expected =
                    new Student("JODO010000001234", "John", "Doe", null);

            when(studentDAO.getStudent(expected.getId()))
                    .thenReturn(expected);

            Student actual =
                    studentQueryService.getStudent(expected.getId());

            assertSame(expected, actual);

            verify(studentDAO).getStudent(expected.getId());
            verifyNoMoreInteractions(studentDAO);
        } catch (StudentNotFoundException e) {
            fail("Unexpected StudentNotFound: " + e.getMessage());
        }
    }

    @Test
    void getStudent_shouldThrowStudentNotFound() throws StudentNotFoundException {

        when(studentDAO.getStudent("INVALID"))
                .thenThrow(new StudentNotFoundException("Student not found"));

        assertThrows(
                StudentNotFoundException.class,
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
    void createStudent_shouldInsertExactlyCapturedStudent() {
        try {
            studentQueryService.createStudent("John", "Doe");

            ArgumentCaptor<Student> captor =
                    ArgumentCaptor.forClass(Student.class);

            verify(studentDAO).insertStudents(captor.capture());

            Student captured = captor.getValue();

            assertEquals("John", captured.getFirstName());
            assertEquals("Doe", captured.getLastName());
            assertNotNull(captured.getId());
        } catch (InvalidNameException e) {
            fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }


    @Test
    void createStudent_shouldSurfaceDatabaseFailure() {

        StudentDaoInterface dao =
                mock(StudentDaoInterface.class);

        doThrow(new DatabaseException(
                "DB unavailable",
                new SQLException()
        ))
                .when(dao)
                .insertStudents(any(Student.class));

        StudentQueryService service =
                new StudentQueryService(dao);


        assertThrows(
                DatabaseException.class,
                () -> service.createStudent(
                        "John",
                        "Doe"
                )
        );
    }
    @Test
    void getStudent_shouldReturnNotFoundOnlyWhenNoStudentExists()
            throws StudentNotFoundException {

        StudentDaoInterface dao =
                mock(StudentDaoInterface.class);

        when(dao.getStudent("123"))
                .thenThrow(new StudentNotFoundException(
                        "Student not found"
                ));

        StudentQueryService service =
                new StudentQueryService(dao);

        assertThrows(
                StudentNotFoundException.class,
                () -> service.getStudent("123")
        );
    }
}
