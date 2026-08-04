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
import org.junit.jupiter.api.Assertions;


class StudentQueryServiceTest {
    private static StudentQueryService studentQueryService;
    @Mock
    private static StudentDaoInterface studentDAO;


    @BeforeAll
    static void setUp() {
        studentDAO = Mockito.mock(StudentDaoInterface.class);
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

            Assertions.assertNotNull(student);
            Assertions.assertEquals("John", student.getFirstName());
            Assertions.assertEquals("Doe", student.getLastName());
            Assertions.assertNotNull(student.getId());

            Mockito.verify(studentDAO).insertStudents(student);
            Mockito.verifyNoMoreInteractions(studentDAO);
        } catch (InvalidNameException e) {
            Assertions.fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldGenerateUniqueIds() {
        try {
            Student student1 = studentQueryService.createStudent("John", "Doe");
            Student student2 = studentQueryService.createStudent("John", "Doe");

            Assertions.assertNotEquals(student1.getId(), student2.getId());

            Mockito.verify(studentDAO, Mockito.times(2)).insertStudents(Mockito.any(Student.class));
            Mockito.verifyNoMoreInteractions(studentDAO);
        } catch (InvalidNameException e) {
            Assertions.fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldGenerateCorrectPrefixes() {
        try {
            studentQueryService.createStudent("John", "Doe");

            ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

            Mockito.verify(studentDAO).insertStudents(captor.capture());

            Student captured = captor.getValue();

            Assertions.assertTrue(captured.getId().startsWith("JODO"));
        } catch (InvalidNameException e) {
            Assertions.fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldPadSingleCharacterNames() {
        try {
            Student student = studentQueryService.createStudent("A", "B");

            Assertions.assertTrue(student.getId().startsWith("AXBX"));
        } catch (InvalidNameException e) {
            Assertions.fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }
    @Test
    void createStudent_shouldRejectNullFirstName() {

        Assertions.assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent(null, "Doe")
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectNullLastName() {

        Assertions.assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John", null)
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectBlankFirstName() {

        Assertions.assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("   ", "Doe")
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectBlankLastName() {

        Assertions.assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John", " ")
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectInvalidFirstName() {

        Assertions.assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John123", "Doe")
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldRejectInvalidLastName() {

        Assertions.assertThrows(
                InvalidNameException.class,
                () -> studentQueryService.createStudent("John", "Doe@123")
        );

        Mockito.verifyNoInteractions(studentDAO);
    }
    @Test
    void getStudent_shouldReturnStudent() {
        try {
            Student expected =
                    new Student("JODO010000001234", "John", "Doe", null);

            Mockito.when(studentDAO.getStudent(expected.getId()))
                    .thenReturn(expected);

            Student actual =
                    studentQueryService.getStudent(expected.getId());

            Assertions.assertSame(expected, actual);

            Mockito.verify(studentDAO).getStudent(expected.getId());
            Mockito.verifyNoMoreInteractions(studentDAO);
        } catch (StudentNotFoundException e) {
            Assertions.fail("Unexpected StudentNotFound: " + e.getMessage());
        }
    }

    @Test
    void getStudent_shouldThrowStudentNotFound() throws StudentNotFoundException {

        Mockito.when(studentDAO.getStudent("INVALID"))
                .thenThrow(new StudentNotFoundException("Student not found"));

        Assertions.assertThrows(
                StudentNotFoundException.class,
                () -> studentQueryService.getStudent("INVALID")
        );

        Mockito.verify(studentDAO).getStudent("INVALID");
        Mockito.verifyNoMoreInteractions(studentDAO);
    }

    @Test
    void getStudent_shouldRejectNullId() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> studentQueryService.getStudent(null)
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void getStudent_shouldRejectBlankId() {

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> studentQueryService.getStudent(" ")
        );

        Mockito.verifyNoInteractions(studentDAO);
    }

    @Test
    void createStudent_shouldInsertExactlyCapturedStudent() {
        try {
            studentQueryService.createStudent("John", "Doe");

            ArgumentCaptor<Student> captor =
                    ArgumentCaptor.forClass(Student.class);

            Mockito.verify(studentDAO).insertStudents(captor.capture());

            Student captured = captor.getValue();

            Assertions.assertEquals("John", captured.getFirstName());
            Assertions.assertEquals("Doe", captured.getLastName());
            Assertions.assertNotNull(captured.getId());
        } catch (InvalidNameException e) {
            Assertions.fail("Unexpected InvalidNameException: " + e.getMessage());
        }
    }


    @Test
    void createStudent_shouldSurfaceDatabaseFailure() {

        StudentDaoInterface dao =
                Mockito.mock(StudentDaoInterface.class);

        Mockito.doThrow(new DatabaseException(
                "DB unavailable",
                new SQLException()
        ))
                .when(dao)
                .insertStudents(Mockito.any(Student.class));

        StudentQueryService service =
                new StudentQueryService(dao);


        Assertions.assertThrows(
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
                Mockito.mock(StudentDaoInterface.class);

        Mockito.when(dao.getStudent("123"))
                .thenThrow(new StudentNotFoundException(
                        "Student not found"
                ));

        StudentQueryService service =
                new StudentQueryService(dao);

        Assertions.assertThrows(
                StudentNotFoundException.class,
                () -> service.getStudent("123")
        );
    }
}
