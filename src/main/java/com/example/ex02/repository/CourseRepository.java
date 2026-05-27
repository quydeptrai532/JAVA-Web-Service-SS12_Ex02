package com.example.ex02.repository;

import com.example.ex02.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CourseRepository {
    private final List<Course> courses = new ArrayList<>();
    private Long nextId = 1L;

    public List<Course> findAll() {
        return courses;
    }

    public Optional<Course> findById(Long id) {
        return courses.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public Course save(Course course) {
        if (course.getId() == null) {
            course.setId(nextId++);
            courses.add(course);
        } else {
            courses.removeIf(c -> c.getId().equals(course.getId()));
            courses.add(course);
        }
        return course;
    }

    public boolean deleteById(Long id) {
        return courses.removeIf(c -> c.getId().equals(id));
    }
}