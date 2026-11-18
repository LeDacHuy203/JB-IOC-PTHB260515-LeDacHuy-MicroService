package com.example.studentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @ElementCollection
    @CollectionTable(name = "student_enrolled_courses", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "course_id")
    @Builder.Default
    private List<Long> enrolled_courses = new ArrayList<>();
}
