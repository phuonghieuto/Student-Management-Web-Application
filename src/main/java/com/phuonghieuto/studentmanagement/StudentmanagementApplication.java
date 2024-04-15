package com.phuonghieuto.studentmanagement;

import com.phuonghieuto.studentmanagement.entity.Student;
import com.phuonghieuto.studentmanagement.repository.StudentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class StudentmanagementApplication {

	@Autowired
	StudentRepository studentRepository;
	public static void main(String[] args) {
		SpringApplication.run(StudentmanagementApplication.class, args);
	}

//	@Bean
//	public ApplicationRunner initStudents() {
//		final Student student = new Student(new ObjectId(),"John", "Snow");
//		final Student student2 = new Student(new ObjectId(), "Anakin", "Skywalker");
//		return args -> studentRepository.saveAll(Arrays.asList(student, student2));
//
//	}
}
