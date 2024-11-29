package com.btvn.buoi41.controller;


import com.btvn.buoi41.dto.CourseDTO;
import com.btvn.buoi41.dto.StudentDTO;
import com.btvn.buoi41.entity.CourseEntity;
import com.btvn.buoi41.entity.RegistrationEntity;
import com.btvn.buoi41.entity.StudentEntity;
import com.btvn.buoi41.repository.CoursesRepository;
import com.btvn.buoi41.repository.RegistrationRepository;
import com.btvn.buoi41.repository.StudentsRepository;
import com.btvn.buoi41.specification.StudentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CoursesRepository coursesRepository;

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        List<StudentEntity> studentsList = studentsRepository.findAll();

        return ResponseEntity.ok(studentsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {
        Optional<StudentEntity> studentEntity = studentsRepository.findById(id);

        return ResponseEntity.ok(studentEntity);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getStudentsByNameKeyword(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) int ageFrom,
            @RequestParam(required = false) int ageTo,
            @RequestParam(required = false) String email
    ) {
        // Cau 2 su dung JPQL
        // List<StudentEntity> studentList = studentsRepository.findByNameLike(name);
        Specification<StudentEntity> spec = Specification
                .where(StudentSpecification.hasName(name))
                .and(StudentSpecification.hasAgeBetween(ageFrom, ageTo))
                .and(StudentSpecification.hasEmail(email));

        List<StudentDTO> studentList = studentsRepository.findAll(spec).stream().map(
            item -> {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setIdStudent(item.getId());
                studentDTO.setStudentName(item.getStudentName());
                studentDTO.setStudentEmail(item.getStudentEmail());

                return studentDTO;
        }).toList();

        return ResponseEntity.ok(studentList);
    }

    @GetMapping("{id}/courses")
    public ResponseEntity<?> getStudentsRegisteredCourses(@PathVariable int id) {
        List<RegistrationEntity> registrationList = registrationRepository.findByStudentId(id);

        List<CourseDTO> courseList = registrationList.stream().map(
        item -> {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setIdCourse(item.getCourse().getId());
            courseDTO.setCourseName(item.getCourse().getCourseTitle());

            return courseDTO;
        }).toList();

        return ResponseEntity.ok(courseList);
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentEntity student) {
        studentsRepository.save(student);

        return ResponseEntity.ok("Added student");
    }

    @PostMapping("/{id}/courses")
    public ResponseEntity<?> addCourses(@PathVariable int id, @RequestBody List<CourseEntity> courses) {
        if (studentsRepository.findById(id).isEmpty()) {
            return ResponseEntity.badRequest().body("Student not found");
        }
        List<RegistrationEntity> registrationList = courses.stream().map(
        item -> {
            RegistrationEntity registrationEntity = new RegistrationEntity();
            if (coursesRepository.existsById(item.getId())) {
                Date now = new Date();
                registrationEntity.setStudent(studentsRepository.findById(id));
                registrationEntity.setCourse(coursesRepository.findById(item.getId()));
                registrationEntity.setRegistrationDate(now);
            }
            return registrationEntity;
        }).toList();

        registrationRepository.saveAll(registrationList);
        return ResponseEntity.ok("Courses registered");
    }

    @PostMapping("/{sid}/courses/{cid}")
    public ResponseEntity<?> addStudentToCourse(@PathVariable int sid, @PathVariable int cid) {
        if (studentsRepository.findById(sid).isEmpty() || !coursesRepository.existsById(cid)) {
            return ResponseEntity.badRequest().body("Student or Course not found");
        }

        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setStudent(studentsRepository.findById(sid));
        registrationEntity.setCourse(coursesRepository.findById(cid));
        registrationEntity.setRegistrationDate(new Date());

        registrationRepository.save(registrationEntity);

        return ResponseEntity.ok("Student added to course");
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable int id, @RequestBody StudentEntity student) {
        Optional<StudentEntity> studentEntity = studentsRepository.findById(id);

        if (studentEntity.isPresent()) {
            StudentEntity s = studentEntity.get();
            s.setStudentName(student.getStudentName());
            s.setStudentAge(student.getStudentAge());

            studentsRepository.save(s);
            return ResponseEntity.ok("Updated student");
        }


        return ResponseEntity.badRequest().body("Student not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        if (studentsRepository.existsById(id)) {
            studentsRepository.deleteById(id);
            return ResponseEntity.ok("Deleted student");
        }

        return ResponseEntity.badRequest().body("Student not found");
    }

    @DeleteMapping("/{sid}/courses/{cid}")
    public ResponseEntity<?> removeStudentFromCourse(@PathVariable int sid, @PathVariable int cid) {
        if (!studentsRepository.existsById(sid) || !coursesRepository.existsById(cid)) {
            return ResponseEntity.badRequest().body("Student or Course not found");
        }


        registrationRepository.removeByStudentIdAndCourseId(sid, cid);

        return ResponseEntity.ok("Student removed from  course");
    }

}
