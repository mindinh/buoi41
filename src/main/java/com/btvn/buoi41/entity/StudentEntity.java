package com.btvn.buoi41.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "students")
@Getter @Setter
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String studentName;

    @Column(name = "age")
    private int studentAge;

    @OneToMany(mappedBy = "student")
    private List<RegistrationEntity> registrations;

}
