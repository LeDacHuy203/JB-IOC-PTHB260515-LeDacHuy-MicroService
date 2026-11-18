package com.example.studentservice.service;

import com.example.studentservice.client.CourseClient;
import com.example.studentservice.dto.CourseResponse;
import com.example.studentservice.dto.StudentResponse;
import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.IStudentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final IStudentRepository iStudentRepository;
    private final CourseClient courseClient;

    @Transactional
    public Student registerCourse(StudentResponse response) {
        if (response.getStudentId() == null || response.getCourseId() == null) {
            throw new IllegalArgumentException("studentId và courseId không được để trống");
        }

        // 1. Kiểm tra khóa học qua OpenFeign
        CourseResponse course;
        try {
            course = courseClient.getById(response.getCourseId());
        } catch (FeignException.NotFound e) {
            log.warn("Không tìm thấy khóa học với ID: {}", response.getCourseId());
            throw new RuntimeException("Không tìm thấy khóa học với ID: " + response.getCourseId());
        } catch (FeignException e) {
            log.warn("Lỗi khi gọi Course Service: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thấy hoặc lỗi dịch vụ với khóa học ID: " + response.getCourseId());
        }

        // 2. Kiểm tra capacity > 0
        if (course == null || course.getCapacity() == null || course.getCapacity() <= 0) {
            log.warn("Khóa học ID: {} đã hết chỗ hoặc không hợp lệ", response.getCourseId());
            throw new RuntimeException("Khóa học đã hết chỗ");
        }

        // 3. Kiểm tra sinh viên có tồn tại không
        Student student = iStudentRepository.findById(response.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên ID: " + response.getStudentId()));

        // 4. Kiểm tra sinh viên đã đăng ký khóa học này chưa
        if (student.getEnrolled_courses() == null) {
            student.setEnrolled_courses(new ArrayList<>());
        }
        if (student.getEnrolled_courses().contains(response.getCourseId())) {
            log.warn("Sinh viên ID: {} đã đăng ký khóa học ID: {}", response.getStudentId(), response.getCourseId());
            throw new RuntimeException("Sinh viên ID: " + response.getStudentId() + " đã đăng ký khóa học này rồi");
        }

        // 5. Cập nhật số lượng capacity của khóa học
        courseClient.updateCapacity(response.getCourseId());

        // 6. Cập nhật danh sách enrolled_courses của sinh viên
        student.getEnrolled_courses().add(response.getCourseId());
        return iStudentRepository.save(student);
    }
}
