package com.phuonghieuto.studentmanagement.service;

import com.phuonghieuto.studentmanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private UserRepository userRepository;

}
