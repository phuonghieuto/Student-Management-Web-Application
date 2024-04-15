package com.phuonghieuto.studentmanagement.service;

import com.phuonghieuto.studentmanagement.entity.Student;
import com.phuonghieuto.studentmanagement.repository.StudentRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
        studentService = new StudentService(studentRepository);
    }

    @Test
    void getStudent_ReturnStudent_WhenStudentExists() {
        final ObjectId id = ObjectId.get();
        final Student student = new Student();
        student.setId(id);
        student.setFirstName("John");
        student.setLastName("Smith");

        final Optional<Student> expected = Optional.of(student);

        given(studentRepository.findById(id)).willReturn(expected);
        final Optional<Student> actual = studentService.getStudent(id);
        assertEquals(expected, actual);

        then(studentRepository).should().findById(id);
        then(studentRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getStudent_ReturnStudent_WhenStudentDoesNotExists() {
        final ObjectId id = ObjectId.get();
        final Student student = new Student();
        student.setId(id);
        student.setFirstName("John");
        student.setLastName("Smith");

        final Optional<Student> expected = Optional.empty();

        given(studentRepository.findById(id)).willReturn(expected);
        final Optional<Student> actual = studentService.getStudent(id);
        assertEquals(expected, actual);

        then(studentRepository).should().findById(id);
        then(studentRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void saveStudent_ReturnSaved_WhenStudentRecordIsCreated() {
        final ObjectId id = ObjectId.get();
        final Student expected = new Student();
        expected.setId(id);
        expected.setFirstName("John");
        expected.setLastName("Smith");


        given(studentRepository.save(expected)).willAnswer(invocation -> {
            final Student toSave = invocation.getArgument(0);

            toSave.setId(id);

            return toSave;
        });
        final Student actual = studentService.saveStudent(expected);
        assertEquals(expected, actual);

        then(studentRepository).should().save(expected);
        then(studentRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteStudent_DeleteStudent_WhenStudentExits() {
        final ObjectId id = ObjectId.get();

        willDoNothing().given(studentRepository).deleteById(id);


        studentService.deleteStudent(id);
        then(studentRepository).should().deleteById(id);
        then(studentRepository).shouldHaveNoMoreInteractions();
    }
}
