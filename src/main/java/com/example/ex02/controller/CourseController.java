package com.example.ex02.controller;

import com.example.ex02.model.Course;
import com.example.ex02.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        // YÊU CẦU: Log INFO mỗi khi có request đến
        logger.info("Method: getAllCourses | Endpoint: GET /api/courses");
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        logger.info("Method: getCourseById | Endpoint: GET /api/courses/{}", id);
        try {
            // Giả lập ném RuntimeException nếu truyền id âm để test ERROR log
            if (id < 0) {
                throw new RuntimeException("ID khóa học không hợp lệ (ID âm)!");
            }

            Optional<Course> course = courseService.getCourseById(id);
            return course.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (RuntimeException e) {
            // YÊU CẦU: Log ERROR khi bắt được RuntimeException
            logger.error("Đã xảy ra RuntimeException trong getCourseById: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        logger.info("Method: createCourse | Endpoint: POST /api/courses");
        try {
            Course createdCourse = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (RuntimeException e) {
            logger.error("Lỗi khi tạo mới khóa học: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        logger.info("Method: updateCourse | Endpoint: PUT /api/courses/{}", id);
        try {
            Optional<Course> updatedCourse = courseService.updateCourse(id, course);
            return updatedCourse.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (RuntimeException e) {
            logger.error("Lỗi khi cập nhật khóa học ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        logger.info("Method: deleteCourse | Endpoint: DELETE /api/courses/{}", id);
        try {
            boolean isDeleted = courseService.deleteCourse(id);
            if (isDeleted) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (RuntimeException e) {
            logger.error("Lỗi khi xóa khóa học ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}