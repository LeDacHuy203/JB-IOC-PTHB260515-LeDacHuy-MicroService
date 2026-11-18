package com.example.studentservice.controller;

import com.example.studentservice.dto.StudentResponse;
import com.example.studentservice.entity.Student;
import com.example.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
// POST /api/students/enroll
    @PostMapping("/enroll")
    public ResponseEntity<Student> enroll(@RequestBody StudentResponse response) {
        return ResponseEntity.ok(studentService.registerCourse(response));
    }
}
