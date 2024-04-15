package com.phuonghieuto.studentmanagement.repository;

import com.phuonghieuto.studentmanagement.entity.Student;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void save_StoresRecord_WhenRecordIsValid() {
        final Student expected = new Student();
        expected.setFirstName("John");
        expected.setLastName("Doe");

        final Student saved = studentRepository.save(expected);
        final Student actual = studentRepository.findById(saved.getId()).get();

        assertEquals(expected, actual);
    }
}
