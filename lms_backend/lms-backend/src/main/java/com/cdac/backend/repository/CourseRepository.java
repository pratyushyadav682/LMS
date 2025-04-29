package com.cdac.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.backend.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
