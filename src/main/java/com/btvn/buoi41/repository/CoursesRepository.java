package com.btvn.buoi41.repository;

import com.btvn.buoi41.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursesRepository extends JpaRepository<CourseEntity, Integer> {
    public CourseEntity findById(int id);

    @Query(value = "SELECT * FROM courses c WHERE c.duration >= ?", nativeQuery = true)
    public List<CourseEntity> findByCourseDurationGreaterThan(int duration);

    @Query(value = "SELECT COUNT(0) FROM courses c", nativeQuery = true)
    public int countAll();

}
