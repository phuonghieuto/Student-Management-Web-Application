package com.phuonghieuto.studentmanagement.service;

import com.phuonghieuto.studentmanagement.entity.Student;
import com.phuonghieuto.studentmanagement.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class StudentService {
    private StudentRepository studentRepository;

    public Page<Student> getStudents(int pageNumber, int pageSize) {
        return studentRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public Optional<Student> getStudent(ObjectId id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(ObjectId id) {
        studentRepository.deleteById(id);
    }
}
