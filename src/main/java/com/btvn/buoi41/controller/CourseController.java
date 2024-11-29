package com.btvn.buoi41.controller;


import com.btvn.buoi41.dto.CourseDTO;
import com.btvn.buoi41.dto.StudentDTO;
import com.btvn.buoi41.entity.CourseEntity;
import com.btvn.buoi41.repository.CoursesRepository;
import com.btvn.buoi41.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CoursesRepository coursesRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        Sort.Direction sortDirection = Sort.Direction.fromString(sort.split(",")[1].toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort.split(",")[0]));

        List<CourseDTO> courseList = coursesRepository.findAll(pageable).stream().map(
            item -> {
                CourseDTO courseDTO  = new CourseDTO();
                courseDTO.setIdCourse(item.getId());
                courseDTO.setCourseName(item.getCourseTitle());

                return courseDTO;
        }).toList();

        return ResponseEntity.ok(courseList);
    }

    // GET api/courses already exists, so I added /jpql for this method
    @GetMapping("/jpql")
    public ResponseEntity<?> getCoursesGreaterThanDuration(@RequestParam int duration) {
        List<CourseEntity> courseEntityList = coursesRepository.findByCourseDurationGreaterThan(duration);

        return ResponseEntity.ok(courseEntityList);
    }

    @GetMapping("{id}/students")
    public ResponseEntity<?> getStudentsRegisteredCourse(@PathVariable int id) {
        if (!coursesRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Course not found");
        }

        List<StudentDTO> studentList = registrationRepository.findByCourseId(id).stream().map(
            item -> {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setIdStudent(item.getStudent().getId());
                studentDTO.setStudentName(item.getStudent().getStudentName());
                studentDTO.setStudentEmail(item.getStudent().getStudentEmail());

                return studentDTO;
        }).toList();


        return ResponseEntity.ok(studentList);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCoursesCount() {

        return ResponseEntity.ok(coursesRepository.countAll());
    }

}
