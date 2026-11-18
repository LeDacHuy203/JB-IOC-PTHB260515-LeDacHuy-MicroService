package com.example.courseservice.service;

import com.example.courseservice.entity.Course;
import com.example.courseservice.repository.ICourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final ICourseRepository iCourseRepository;
    public Course getById(Long courseId){
        Course course = iCourseRepository.findById(courseId)
                .orElseThrow(()->new RuntimeException("Không tìm thấy Id lớp: "+ courseId));
        return  course;
    }
//    Cập nhật số lượng học viên và danh sách enrolled_courses tương ứng.
    @Transactional
    public Course updateCapacityById(Long courseId) {
        Course course = iCourseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Id lớp: " + courseId));

        if (course.getCapacity() == null || course.getCapacity() <= 0) {
            throw new RuntimeException("Lớp đã hết chỗ: " + courseId);
        }
        course.setCapacity(course.getCapacity() - 1);
        return course;
    }

}
