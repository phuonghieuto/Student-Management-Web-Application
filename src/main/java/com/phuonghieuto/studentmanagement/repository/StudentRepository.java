package com.phuonghieuto.studentmanagement.repository;

import com.phuonghieuto.studentmanagement.entity.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, ObjectId> {

}
