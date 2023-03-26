package com.skd.service;

import com.skd.entity.Student;
import com.skd.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;


    public Student findById(String username) {
        return studentRepository.findById(username).orElse(null);
    }


    public List<Student> findAll() {
        return studentRepository.findAll();
    }


    public Student save(Student student){
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }
}
