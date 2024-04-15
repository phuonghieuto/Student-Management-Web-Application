package com.phuonghieuto.studentmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Document(collection = "students")
public class Student {
    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;
}
