package com.example.studentservice.service;

import com.example.studentservice.client.CourseClient;
import com.example.studentservice.dto.CourseResponse;
import com.example.studentservice.dto.StudentResponse;
import com.example.studentservice.entity.Student;
import com.example.studentservice.repository.IStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {
    private final IStudentRepository iStudentRepository;
    private final CourseClient courseClient;

    @Transactional
    public Student registerCourse(StudentResponse response) {
        CourseResponse course = courseClient.getById(response.getCourseId());
//      Kiểm tra xem khoá học có null hay không hoặc nhỏ hơn 0
        if (course == null || course.getCapacity() <= 0) {
            log.warn("Khoá học hết chỗ hoặc không tồn tại");
            throw new RuntimeException("Khoá học hết chỗ hoặc không tồn tại");
        }
        Student student = iStudentRepository.findById(response.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));
//        Kiểm tra xem sinh viên đã có trong lớp chưa
        if(student.getEnrolled_courses().contains(response.getCourseId())){
            log.warn("Sinh vien ID: "+response.getStudentId()+"đã đăng ký khoá học");
            throw new RuntimeException("Sinh vien ID: "+response.getStudentId()+"đã đăng ký khoá học");
        }
        courseClient.updateCapacity(response.getCourseId());
        student.getEnrolled_courses().add(response.getCourseId());

        return iStudentRepository.save(student);
    }


}
