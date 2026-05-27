package com.example.ex02.service;

import com.example.ex02.model.Course;
import com.example.ex02.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            // YÊU CẦU: Log WARN khi không tìm thấy Course
            logger.warn("Không tìm thấy khóa học với ID: {}", id);
        }
        return course;
    }

    public Course createCourse(Course course) {
        Course savedCourse = courseRepository.save(course);
        // YÊU CẦU: Log INFO khi tạo mới thành công
        logger.info("Tạo mới thành công khóa học: {} (ID: {})", savedCourse.getCourseName(), savedCourse.getId());
        return savedCourse;
    }

    public Optional<Course> updateCourse(Long id, Course courseDetails) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isEmpty()) {
            logger.warn("Cập nhật thất bại. Không tìm thấy khóa học với ID: {}", id);
            return Optional.empty();
        }
        courseDetails.setId(id);
        Course updatedCourse = courseRepository.save(courseDetails);

        // YÊU CẦU: Log INFO khi cập nhật thành công
        logger.info("Cập nhật thành công khóa học ID: {}", id);
        return Optional.of(updatedCourse);
    }

    public boolean deleteCourse(Long id) {
        boolean isDeleted = courseRepository.deleteById(id);
        if (!isDeleted) {
            logger.warn("Xóa thất bại. Không tìm thấy khóa học với ID: {}", id);
        }
        return isDeleted;
    }
}