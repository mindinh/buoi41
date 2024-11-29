package com.btvn.buoi41.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Entity(name = "registration")
@Data
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int registration_id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;


    @Column(name = "registration_date")
    private Date registrationDate;

    public void setStudent(Optional<StudentEntity> student) {
        this.student = student.orElse(null);
    }

}
