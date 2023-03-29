package com.skd.controller;

import com.skd.dto.AuthenticateRequest;
import com.skd.entity.Student;
import com.skd.repository.StudentRepository;
import com.skd.service.JwtService;
import com.skd.service.StudentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostConstruct
    public void initialize(){
//        Student student1 = Student.builder()
//                .name("sumeet")
//                .password("pass")
//                .roles("admin,user")
//                .build();
//        Student student2 = Student.builder()
//                .name("Archana Vishwakarma")
//                .build();
//        Student student3 = Student.builder()
//                .name("Ajay Dwivedi")
//                .build();
//
//        studentRepository.save(student1);
//        studentRepository.save(student2);
//        studentRepository.save(student3);
    }

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(){
        AuthenticateRequest authenticateRequest = new AuthenticateRequest();
        authenticateRequest.setUsername("shiva");
        authenticateRequest.setPassword("pass");
        log.info("Generated JWT Token is {}",authenticateRequest(authenticateRequest));

        return new ResponseEntity<>("Welcome Students", HttpStatus.OK);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Student> getStudentById(@PathVariable String username){
        Student student = studentService.findById(username);
        log.info("Fetched Student is {}",student);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/fetchAll")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents(){
        List<Student> students = studentService.findAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/save")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Student> saveStudent(@RequestBody Student student){
        Student s = studentService.save(student);
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public String authenticateRequest(@RequestBody AuthenticateRequest authenticateRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword()));
        if(authentication.isAuthenticated())
        return jwtService.generateToken(authenticateRequest.getUsername());
        else
        throw new UsernameNotFoundException("User doesn't exist");
    }
}
