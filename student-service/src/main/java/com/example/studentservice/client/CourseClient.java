package com.example.studentservice.client;

import com.example.studentservice.dto.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/api/courses/{courseId}")
    CourseResponse getById(@PathVariable("courseId") Long courseId);

    @PutMapping("/api/courses/{courseId}/capacity")
    CourseResponse updateCapacity(@PathVariable("courseId") Long courseId);
}
