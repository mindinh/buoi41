package com.btvn.buoi41.specification;

import com.btvn.buoi41.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<StudentEntity> hasName(String name) {
        return (root, query, criteriaBuilder) ->
            name != null ? criteriaBuilder.like(root.get("studentName"), "%" + name + "%") : null;
    }

    public static Specification<StudentEntity> hasAgeBetween(int min, int max) {
        return ((root, query, criteriaBuilder) ->
            (min > 0 && max > 0 && max > min) ? criteriaBuilder.between(root.get("studentAge"), min, max) : null);
    }

    public static Specification<StudentEntity> hasEmail(String email) {
        return ((root, query, criteriaBuilder) ->
            email != null ? criteriaBuilder.like(root.get("studentEmail"), "%" + email + "%") : null);
    }
}
