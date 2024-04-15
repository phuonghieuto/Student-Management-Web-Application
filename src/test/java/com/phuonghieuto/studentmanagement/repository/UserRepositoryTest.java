package com.phuonghieuto.studentmanagement.repository;

import com.phuonghieuto.studentmanagement.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void save_StoresRecord_WhenRecordIsValid() {
        final User expected = new User();
        expected.setUsername("phuonghieuto");
        expected.setPassword("123456");

        final User saved = userRepository.save(expected);
        final User actual = userRepository.findById(saved.getId()).get();

        assertEquals(expected, actual);
    }
}