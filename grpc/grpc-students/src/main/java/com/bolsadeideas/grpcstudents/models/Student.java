package com.bolsadeideas.grpcstudents.models;

import com.bolsadeideas.grpcstudents.entities.StudentEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Student {

    private Long id;

    private String name;

    private School school;

    public Student(StudentEntity studentEntity, School school) {
        this.id = studentEntity.getId();
        this.name = studentEntity.getName();
        this.school = school;
    }

}
