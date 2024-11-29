package com.btvn.buoi41.repository;

import com.btvn.buoi41.entity.RegistrationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {
    public List<RegistrationEntity> findByStudentId(int student_id);

    public List<RegistrationEntity> findByCourseId(int course_id);

    @Transactional
    public void removeByStudentIdAndCourseId(int student_id, int course_id);


}
