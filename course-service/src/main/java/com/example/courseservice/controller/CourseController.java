package com.example.courseservice.controller;

import com.example.courseservice.entity.Course;
import com.example.courseservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getById(@PathVariable Long courseId) {
        Course result = courseService.getById(courseId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{courseId}/capacity")
    public ResponseEntity<Course> updateCapacity(@PathVariable Long courseId) {
        return ResponseEntity.ok(courseService.updateCapacityById(courseId));
    }
}
