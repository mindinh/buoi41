package com.btvn.buoi41.repository;

import com.btvn.buoi41.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentsRepository extends JpaRepository<StudentEntity, Integer> {

    @Query(value = "SELECT * FROM students s WHERE s.name LIKE CONCAT('%', :name, '%')", nativeQuery = true)
    public List<StudentEntity> findByNameLike(String name);

    public List<StudentEntity> findByStudentNameContaining(String name);


}
