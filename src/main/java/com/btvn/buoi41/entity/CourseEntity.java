package com.btvn.buoi41.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity(name = "courses")
@Data
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String courseTitle;

    @Column(name = "duration")
    private int courseDuration;




}
