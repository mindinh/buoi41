package com.btvn.buoi41.specification;

import com.btvn.buoi41.entity.StudentEntity;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {
    public static Specification<StudentEntity> hasName(String name) {
        return (root, query, criteriaBuilder) ->
            name != null ? criteriaBuilder.like(root.get("studentName"), "%" + name + "%") : null;
    }

    public static Specification<StudentEntity> hasAgeBetween(Integer min, Integer max) {
        return ((root, query, criteriaBuilder) -> {
            if (min != null && max != null) {
                return criteriaBuilder.between(root.get("studentAge"), min, max);
            }
            else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("studentAge"), min);
            }
            else if (max != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("studentAge"), max);
            }
            else {
                return null;
            }
        });
    }

    public static Specification<StudentEntity> hasEmail(String email) {
        return ((root, query, criteriaBuilder) ->
            email != null ? criteriaBuilder.like(root.get("studentEmail"), "%" + email) : null);
    }
}
