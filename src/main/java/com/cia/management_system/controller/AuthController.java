//package com.cia.management_system.controller;
//
//
//
//import com.cia.management_system.dto.JwtResponse;
//import com.cia.management_system.dto.LoginRequest;
//import com.cia.management_system.dto.MessageResponse;
//import com.cia.management_system.dto.SignupRequest;
//import com.cia.management_system.service.AuthService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
///**
// * Authentication Controller
// * Handles login and signup endpoints
// */
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*", maxAge = 3600)
//public class AuthController {
//
//    @Autowired
//    private AuthService authService;
//
//    /**
//     * Login endpoint
//     * POST /api/auth/login
//     */
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
//        try {
//            JwtResponse response = authService.login(loginRequest);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(MessageResponse.error("Invalid username/email or password"));
//        }
//    }
//
//    /**
//     * Signup endpoint
//     * POST /api/auth/signup
//     */
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
//        try {
//            MessageResponse response = authService.signup(signupRequest);
//
//            if (response.isSuccess()) {
//                return ResponseEntity
//                        .status(HttpStatus.CREATED)
//                        .body(response);
//            } else {
//                return ResponseEntity
//                        .status(HttpStatus.BAD_REQUEST)
//                        .body(response);
//            }
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(MessageResponse.error("Registration failed: " + e.getMessage()));
//        }
//    }
//
//    /**
//     * Test endpoint (public)
//     * GET /api/auth/test
//     */
//    @GetMapping("/test")
//    public ResponseEntity<?> test() {
//        return ResponseEntity.ok(MessageResponse.success("CIA Management System API is running"));
//    }
//
//    /**
//     * Health check endpoint
//     * GET /api/auth/health
//     */
//    @GetMapping("/health")
//    public ResponseEntity<?> health() {
//        return ResponseEntity.ok(MessageResponse.success("API is healthy"));
//    }
//}


package com.cia.management_system.controller;

import com.cia.management_system.dto.JwtResponse;
import com.cia.management_system.dto.LoginRequest;
import com.cia.management_system.dto.MessageResponse;
import com.cia.management_system.dto.SignupRequest;
import com.cia.management_system.model.Role;
import com.cia.management_system.model.Student;
import com.cia.management_system.model.User;
import com.cia.management_system.repository.FacultyRepository;
import com.cia.management_system.repository.StudentRepository;
import com.cia.management_system.repository.UserRepository;
import com.cia.management_system.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Authentication Controller
 * Handles login and signup endpoints
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil; // Using JwtUtil instead of JwtTokenProvider
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Login endpoint
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token using JwtUtil
            String jwt = jwtUtil.generateToken(authentication);

            // Get user details
            User user = userRepository.findByUsername(loginRequest.getUsernameOrEmail())
                    .or(() -> userRepository.findByEmail(loginRequest.getUsernameOrEmail()))
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update last login
            user.updateLastLogin();
            userRepository.save(user);

            // Build user details object
            JwtResponse.UserDetails userDetails = buildUserDetails(user);

            // Create response
            JwtResponse response = new JwtResponse(jwt, userDetails);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(MessageResponse.error("Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Login failed: " + e.getMessage()));
        }
    }

    /**
     * Signup endpoint
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            // Check if username exists
            if (userRepository.existsByUsername(signupRequest.getUsername())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.error("Username is already taken"));
            }

            // Check if email exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.error("Email is already in use"));
            }

            // Create new user
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
            user.setFullName(signupRequest.getFullName());

            // Set role (default to STUDENT if not specified)
            String roleStr = signupRequest.getRole() != null ?
                    signupRequest.getRole().toUpperCase() : "STUDENT";
            Role role = Role.valueOf(roleStr);

            // Set roles (as Set) - add the primary role
            user.addRole(role);
            user.setEnabled(true);
            user.setAccountNonLocked(true);

            // Save user
            User savedUser = userRepository.save(user);

            // Create role-specific profile
            if (role == Role.STUDENT && signupRequest.getRollNumber() != null) {
                createStudentProfile(savedUser, signupRequest);
            } else if (role == Role.FACULTY && signupRequest.getEmployeeId() != null) {
                createFacultyProfile(savedUser, signupRequest);
            }

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MessageResponse.success("User registered successfully"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.error("Invalid role specified"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Test endpoint (public)
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(MessageResponse.success("CIA Management System API is running"));
    }

    /**
     * Health check endpoint
     * GET /api/auth/health
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(MessageResponse.success("API is healthy"));
    }

    // ==================== HELPER METHODS ====================

    /**
     * Build user details based on role
     */
    private JwtResponse.UserDetails buildUserDetails(User user) {
        JwtResponse.UserDetails details = new JwtResponse.UserDetails();

        // Common fields
        details.setId(user.getId());
        details.setUsername(user.getUsername());
        details.setEmail(user.getEmail());
        details.setFullName(user.getFullName());

        // Get primary role - handle multiple roles
        Role primaryRole = user.getPrimaryRole();
        details.setRole(primaryRole != null ? primaryRole.name() : "USER");

        // Role-specific fields
        if (primaryRole == Role.STUDENT) {
            studentRepository.findByUserId(user.getId()).ifPresent(student -> {
                details.setStudentId(student.getId());
                details.setRollNumber(student.getRollNumber());
                details.setSemester(student.getSemester());
                details.setStream(String.valueOf(student.getStream()));
                details.setSection(student.getSection());
                details.setAcademicYear(student.getAcademicYear());
            });
        } else if (primaryRole == Role.FACULTY) {
            facultyRepository.findByUserId(user.getId()).ifPresent(faculty -> {
                details.setFacultyId(faculty.getId());
                details.setEmployeeId(faculty.getEmployeeId());
                details.setDepartment(faculty.getDepartment());
                details.setDesignation(faculty.getDesignation());
            });
        }

        return details;
    }

    /**
     * Create student profile
     */
    private void createStudentProfile(User user, SignupRequest request) {
        com.cia.management_system.model.Student student = new com.cia.management_system.model.Student();
        student.setUser(user);
        student.setRollNumber(request.getRollNumber());
        student.setSemester(request.getSemester() != null ? request.getSemester() : 1);

        // Handle stream conversion safely
        Student.Stream stream;
        try {
            stream = Student.Stream.valueOf(request.getStream() != null ?
                    request.getStream().toUpperCase() : "BCA");
        } catch (IllegalArgumentException e) {
            stream = Student.Stream.BCA; // Default stream
        }
        student.setStream(stream);

        student.setSection(request.getSection() != null ? request.getSection() : "A");
//        student.setAcademicYear(request.getAcademicYear() != null ?
//                request.getAcademicYear() : "2024-2025");
        studentRepository.save(student);
    }

    /**
     * Create faculty profile
     */
    private void createFacultyProfile(User user, SignupRequest request) {
        com.cia.management_system.model.Faculty faculty = new com.cia.management_system.model.Faculty();
        faculty.setUser(user);
        faculty.setEmployeeId(request.getEmployeeId());
        faculty.setDepartment(request.getDepartment() != null ?
                request.getDepartment() : "Computer Science & Engineering");
        faculty.setDesignation(request.getDesignation() != null ?
                request.getDesignation() : "Assistant Professor");
        facultyRepository.save(faculty);
    }
}