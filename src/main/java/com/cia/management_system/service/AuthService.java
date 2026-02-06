package com.cia.management_system.service;



import com.cia.management_system.dto.JwtResponse;
import com.cia.management_system.dto.LoginRequest;
import com.cia.management_system.dto.MessageResponse;
import com.cia.management_system.dto.SignupRequest;
import com.cia.management_system.model.Faculty;
import com.cia.management_system.model.Role;
import com.cia.management_system.model.Student;
import com.cia.management_system.model.User;
import com.cia.management_system.repository.FacultyRepository;
import com.cia.management_system.repository.StudentRepository;
import com.cia.management_system.repository.UserRepository;
import com.cia.management_system.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authentication Service
 * Handles user registration and login
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * User login
     */
    @Transactional
    public JwtResponse login(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtil.generateToken(authentication);

        // Get user details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Fetch user from database
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update last login
        user.updateLastLogin();
        userRepository.save(user);

        // Build response with additional info based on role
        String rollNumber = null;
        String employeeId = null;
        Integer semester = null;
        String stream = null;

        if (user.hasRole(Role.STUDENT) && user.getStudent() != null) {
            Student student = user.getStudent();
            rollNumber = student.getRollNumber();
            semester = student.getSemester();
            stream = student.getStream().name();
        } else if (user.hasRole(Role.FACULTY) && user.getFaculty() != null) {
            Faculty faculty = user.getFaculty();
            employeeId = faculty.getEmployeeId();
        }

        return new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                roles,
                rollNumber,
                employeeId,
                semester,
                stream
        );
    }

    /**
     * User registration
     */
    @Transactional
    public MessageResponse signup(SignupRequest signupRequest) {
        // Validate username
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return MessageResponse.error("Username is already taken");
        }

        // Validate email
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return MessageResponse.error("Email is already in use");
        }

        // Validate role
        Role role = signupRequest.getRoleEnum();
        if (role == null) {
            return MessageResponse.error("Invalid role");
        }

        // Role-specific validation
        if (role == Role.STUDENT) {
            if (signupRequest.getRollNumber() == null || signupRequest.getRollNumber().isBlank()) {
                return MessageResponse.error("Roll number is required for students");
            }
            if (studentRepository.existsByRollNumber(signupRequest.getRollNumber())) {
                return MessageResponse.error("Roll number is already registered");
            }
        } else if (role == Role.FACULTY) {
            if (signupRequest.getEmployeeId() == null || signupRequest.getEmployeeId().isBlank()) {
                return MessageResponse.error("Employee ID is required for faculty");
            }
            if (facultyRepository.existsByEmployeeId(signupRequest.getEmployeeId())) {
                return MessageResponse.error("Employee ID is already registered");
            }
        }

        // Create user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.addRole(role);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        // Save user first
        user = userRepository.save(user);

        // Create role-specific profile
        if (role == Role.STUDENT) {
            Student student = new Student();
            student.setRollNumber(signupRequest.getRollNumber());
            student.setSemester(signupRequest.getSemester());
            student.setSection(signupRequest.getSection());

            try {
                student.setStream(Student.Stream.valueOf(signupRequest.getStream().toUpperCase()));
            } catch (Exception e) {
                student.setStream(Student.Stream.BCA); // Default
            }

            student.setUser(user);
            student.extractAdmissionYear();
            student.generateAcademicYear();

            studentRepository.save(student);

        } else if (role == Role.FACULTY) {
            Faculty faculty = new Faculty();
            faculty.setEmployeeId(signupRequest.getEmployeeId());
            faculty.setDepartment(signupRequest.getDepartment());
            faculty.setDesignation(signupRequest.getDesignation());
            faculty.setUser(user);

            facultyRepository.save(faculty);
        }

        return MessageResponse.success("User registered successfully");
    }

    /**
     * Get current authenticated user
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Check if current user has specific role
     */
    public boolean hasRole(Role role) {
        User user = getCurrentUser();
        return user != null && user.hasRole(role);
    }
}
