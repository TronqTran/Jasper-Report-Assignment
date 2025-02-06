package com.fpt.report.services;

import com.fpt.report.models.Student;
import com.fpt.report.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer rollNumber) {
        return studentRepository.findById(rollNumber).
                orElseThrow(() -> new RuntimeException("Student not found" + rollNumber));
    }

}
