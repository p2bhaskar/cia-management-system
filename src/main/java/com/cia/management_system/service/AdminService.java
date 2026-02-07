////package com.cia.management_system.service;
////
////
////
////import com.cia.management_system.dto.CreateUserRequest;
////import com.cia.management_system.dto.UserDTO;
////import com.cia.management_system.model.*;
////import com.cia.management_system.repository.*;
////import lombok.RequiredArgsConstructor;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.time.LocalDateTime;
////import java.util.List;
////import java.util.stream.Collectors;
////
////@Service
////@RequiredArgsConstructor
////public class AdminService {
////
////    private final UserRepository userRepository;
////    private final StudentRepository studentRepository;
////    private final FacultyRepository facultyRepository;
////    private final PasswordEncoder passwordEncoder;
////
////    @Transactional
////    public UserDTO createStudent(CreateUserRequest request) {
////        // Check if username already exists
////        if (userRepository.existsByUsername(request.getUsername())) {
////            throw new RuntimeException("Username already exists");
////        }
////
////        // Check if roll number already exists
////        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
////            throw new RuntimeException("Roll number already exists");
////        }
////
////        // Create user
////        User user = new User();
////        user.setUsername(request.getUsername());
////        user.setPassword(passwordEncoder.encode(request.getPassword()));
////        user.setEmail(request.getEmail());
////        user.setFullName(request.getFullName());
////        user.setRole(Role.STUDENT);
////        user.setCreatedAt(LocalDateTime.now());
////        user.setIsActive(true);
////
////        User savedUser = userRepository.save(user);
////
////        // Create student profile
////        Student student = new Student();
////        student.setUser(savedUser);
////        student.setRollNumber(request.getRollNumber());
////        student.setSemester(request.getSemester());
////        student.setStream(Student.Stream.valueOf(request.getStream()));
////        student.setSection(request.getSection());
////        student.setAcademicYear(request.getAcademicYear());
////
////        studentRepository.save(student);
////
////        return convertToUserDTO(savedUser);
////    }
////
////    @Transactional
////    public UserDTO createFaculty(CreateUserRequest request) {
////        if (userRepository.existsByUsername(request.getUsername())) {
////            throw new RuntimeException("Username already exists");
////        }
////
////        if (facultyRepository.existsByEmployeeId(request.getEmployeeId())) {
////            throw new RuntimeException("Employee ID already exists");
////        }
////
////        // Create user
////        User user = new User();
////        user.setUsername(request.getUsername());
////        user.setPassword(passwordEncoder.encode(request.getPassword()));
////        user.setEmail(request.getEmail());
////        user.setFullName(request.getFullName());
////        user.setRole(Role.FACULTY);
////        user.setCreatedAt(LocalDateTime.now());
////        user.setIsActive(true);
////
////        User savedUser = userRepository.save(user);
////
////        // Create faculty profile
////        Faculty faculty = new Faculty();
////        faculty.setUser(savedUser);
////        faculty.setEmployeeId(request.getEmployeeId());
////        faculty.setDepartment(request.getDepartment());
////        faculty.setDesignation(request.getDesignation());
////
////        facultyRepository.save(faculty);
////
////        return convertToUserDTO(savedUser);
////    }
////
////    @Transactional
////    public UserDTO createAdmin(CreateUserRequest request) {
////        if (userRepository.existsByUsername(request.getUsername())) {
////            throw new RuntimeException("Username already exists");
////        }
////
////        User user = new User();
////        user.setUsername(request.getUsername());
////        user.setPassword(passwordEncoder.encode(request.getPassword()));
////        user.setEmail(request.getEmail());
////        user.setFullName(request.getFullName());
////        user.setRole(Role.ADMIN);
////        user.setCreatedAt(LocalDateTime.now());
////        user.setIsActive(true);
////
////        User savedUser = userRepository.save(user);
////
////        return convertToUserDTO(savedUser);
////    }
////
////    @Transactional(readOnly = true)
////    public List<UserDTO> getAllStudents() {
////        return studentRepository.findAll().stream()
////                .map(student -> convertToUserDTO(student.getUser()))
////                .collect(Collectors.toList());
////    }
////
////    @Transactional(readOnly = true)
////    public List<UserDTO> getAllFaculty() {
////        return facultyRepository.findAll().stream()
////                .map(faculty -> convertToUserDTO(faculty.getUser()))
////                .collect(Collectors.toList());
////    }
////
////    @Transactional(readOnly = true)
////    public List<UserDTO> getAllAdmins() {
////        return userRepository.findByRole(Role.ADMIN).stream()
////                .map(this::convertToUserDTO)
////                .collect(Collectors.toList());
////    }
////
////    @Transactional(readOnly = true)
////    public UserDTO getUserById(Long userId) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////        return convertToUserDTO(user);
////    }
////
////    @Transactional
////    public UserDTO updateUser(Long userId, CreateUserRequest request) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        user.setEmail(request.getEmail());
////        user.setFullName(request.getFullName());
////
////        // Update password only if provided
////        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
////            user.setPassword(passwordEncoder.encode(request.getPassword()));
////        }
////
////        User updatedUser = userRepository.save(user);
////
////        // Update role-specific fields
////        if (user.getRole() == Role.STUDENT) {
////            studentRepository.findByUserId(userId).ifPresent(student -> {
////                student.setSemester(request.getSemester());
////                student.setSection(request.getSection());
////                studentRepository.save(student);
////            });
////        } else if (user.getRole() == Role.FACULTY) {
////            facultyRepository.findByUserId(userId).ifPresent(faculty -> {
////                faculty.setDepartment(request.getDepartment());
////                faculty.setDesignation(request.getDesignation());
////                facultyRepository.save(faculty);
////            });
////        }
////
////        return convertToUserDTO(updatedUser);
////    }
////
////    @Transactional
////    public void deleteUser(Long userId) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        user.setIsActive(false); // Soft delete
////        userRepository.save(user);
////    }
////
////    @Transactional
////    public void activateUser(Long userId) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        user.setIsActive(true);
////        userRepository.save(user);
////    }
////
////    @Transactional(readOnly = true)
////    public DashboardStats getDashboardStats() {
////        DashboardStats stats = new DashboardStats();
////        stats.setTotalStudents(studentRepository.count());
////        stats.setTotalFaculty(facultyRepository.count());
////        stats.setTotalAdmins(userRepository.countByRole(Role.ADMIN));
////        stats.setActiveUsers(userRepository.countByIsActiveTrue());
////        return stats;
////    }
////
////    private UserDTO convertToUserDTO(User user) {
////        UserDTO dto = new UserDTO();
////        dto.setId(user.getId());
////        dto.setUsername(user.getUsername());
////        dto.setEmail(user.getEmail());
////        dto.setFullName(user.getFullName());
////        dto.setRole(user.getRole().name());
////        dto.setIsActive(user.getIsActive());
////        dto.setCreatedAt(user.getCreatedAt());
////
////        // Add role-specific fields
////        if (user.getRole() == Role.STUDENT) {
////            studentRepository.findByUserId(user.getId()).ifPresent(student -> {
////                dto.setRollNumber(student.getRollNumber());
////                dto.setSemester(student.getSemester());
////                dto.setStream(String.valueOf(student.getStream()));
////                dto.setSection(student.getSection());
////            });
////        } else if (user.getRole() == Role.FACULTY) {
////            facultyRepository.findByUserId(user.getId()).ifPresent(faculty -> {
////                dto.setEmployeeId(faculty.getEmployeeId());
////                dto.setDepartment(faculty.getDepartment());
////                dto.setDesignation(faculty.getDesignation());
////            });
////        }
////
////        return dto;
////    }
////
////    @lombok.Data
////    public static class DashboardStats {
////        private Long totalStudents;
////        private Long totalFaculty;
////        private Long totalAdmins;
////        private Long activeUsers;
////    }
////}
//
//
//package com.cia.management_system.service;
//
//import com.cia.management_system.dto.CreateUserRequest;
//import com.cia.management_system.dto.UserDTO;
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AdminService {
//
//    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//    private final FacultyRepository facultyRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    // ===================== CREATE =====================
//
//    @Transactional
//    public UserDTO createStudent(CreateUserRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
//            throw new RuntimeException("Roll number already exists");
//        }
//
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setEmail(request.getEmail());
//        user.setFullName(request.getFullName());
//        user.getRoles().add(Role.STUDENT);
//        user.setEnabled(true);
//        user.setCreatedAt(LocalDateTime.now());
//
//        User savedUser = userRepository.save(user);
//
//        Student student = new Student();
//        student.setUser(savedUser);
//        student.setRollNumber(request.getRollNumber());
//        student.setSemester(request.getSemester());
//        student.setStream(Student.Stream.valueOf(request.getStream()));
//        student.setSection(request.getSection());
//        student.setAcademicYear(request.getAcademicYear());
//
//        studentRepository.save(student);
//
//        return convertToUserDTO(savedUser);
//    }
//
//    @Transactional
//    public UserDTO createFaculty(CreateUserRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        if (facultyRepository.existsByEmployeeId(request.getEmployeeId())) {
//            throw new RuntimeException("Employee ID already exists");
//        }
//
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setEmail(request.getEmail());
//        user.setFullName(request.getFullName());
//        user.getRoles().add(Role.FACULTY);
//        user.setEnabled(true);
//        user.setCreatedAt(LocalDateTime.now());
//
//        User savedUser = userRepository.save(user);
//
//        Faculty faculty = new Faculty();
//        faculty.setUser(savedUser);
//        faculty.setEmployeeId(request.getEmployeeId());
//        faculty.setDepartment(request.getDepartment());
//        faculty.setDesignation(request.getDesignation());
//
//        facultyRepository.save(faculty);
//
//        return convertToUserDTO(savedUser);
//    }
//
//    @Transactional
//    public UserDTO createAdmin(CreateUserRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setEmail(request.getEmail());
//        user.setFullName(request.getFullName());
//        user.getRoles().add(Role.ADMIN);
//        user.setEnabled(true);
//        user.setCreatedAt(LocalDateTime.now());
//
//        User savedUser = userRepository.save(user);
//
//        return convertToUserDTO(savedUser);
//    }
//
//    // ===================== READ =====================
//
//    @Transactional(readOnly = true)
//    public List<UserDTO> getAllStudents() {
//        return studentRepository.findAll()
//                .stream()
//                .map(student -> convertToUserDTO(student.getUser()))
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<UserDTO> getAllFaculty() {
//        return facultyRepository.findAll()
//                .stream()
//                .map(faculty -> convertToUserDTO(faculty.getUser()))
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<UserDTO> getAllAdmins() {
//        return userRepository.findByRolesContaining(Role.ADMIN)
//                .stream()
//                .map(this::convertToUserDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public UserDTO getUserById(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        return convertToUserDTO(user);
//    }
//
//    // ===================== UPDATE =====================
//
//    @Transactional
//    public UserDTO updateUser(Long userId, CreateUserRequest request) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setEmail(request.getEmail());
//        user.setFullName(request.getFullName());
//
//        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(request.getPassword()));
//        }
//
//        User updatedUser = userRepository.save(user);
//
//        if (user.getRoles().contains(Role.STUDENT)) {
//            studentRepository.findByUserId(userId).ifPresent(student -> {
//                student.setSemester(request.getSemester());
//                student.setSection(request.getSection());
//                studentRepository.save(student);
//            });
//        } else if (user.getRoles().contains(Role.FACULTY)) {
//            facultyRepository.findByUserId(userId).ifPresent(faculty -> {
//                faculty.setDepartment(request.getDepartment());
//                faculty.setDesignation(request.getDesignation());
//                facultyRepository.save(faculty);
//            });
//        }
//
//        return convertToUserDTO(updatedUser);
//    }
//
//    // ===================== STATUS =====================
//
//    @Transactional
//    public void deleteUser(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setEnabled(false);
//        userRepository.save(user);
//    }
//
//    @Transactional
//    public void activateUser(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setEnabled(true);
//        userRepository.save(user);
//    }
//
//    // ===================== DASHBOARD =====================
//
//    @Transactional(readOnly = true)
//    public DashboardStats getDashboardStats() {
//        DashboardStats stats = new DashboardStats();
//        stats.setTotalStudents(studentRepository.count());
//        stats.setTotalFaculty(facultyRepository.count());
//        stats.setTotalAdmins(userRepository.countByRolesContaining(Role.ADMIN));
//        stats.setActiveUsers(userRepository.countByEnabledTrue());
//        return stats;
//    }
//
//    // ===================== MAPPER =====================
//
//    private UserDTO convertToUserDTO(User user) {
//        UserDTO dto = new UserDTO();
//        dto.setId(user.getId());
//        dto.setUsername(user.getUsername());
//        dto.setEmail(user.getEmail());
//        dto.setFullName(user.getFullName());
//        dto.setIsActive(user.getEnabled());
//        dto.setCreatedAt(user.getCreatedAt());
//
//        Role primaryRole = user.getPrimaryRole();
//        if (primaryRole != null) {
//            dto.setRole(primaryRole.name());
//        }
//
//        if (primaryRole == Role.STUDENT) {
//            studentRepository.findByUserId(user.getId()).ifPresent(student -> {
//                dto.setRollNumber(student.getRollNumber());
//                dto.setSemester(student.getSemester());
//                dto.setStream(student.getStream().name());
//                dto.setSection(student.getSection());
//            });
//        } else if (primaryRole == Role.FACULTY) {
//            facultyRepository.findByUserId(user.getId()).ifPresent(faculty -> {
//                dto.setEmployeeId(faculty.getEmployeeId());
//                dto.setDepartment(faculty.getDepartment());
//                dto.setDesignation(faculty.getDesignation());
//            });
//        }
//
//        return dto;
//    }
//
//    // ===================== INNER DTO =====================
//
//    @lombok.Data
//    public static class DashboardStats {
//        private Long totalStudents;
//        private Long totalFaculty;
//        private Long totalAdmins;
//        private Long activeUsers;
//    }
//}


package com.cia.management_system.service;

import com.cia.management_system.dto.CreateUserRequest;
import com.cia.management_system.dto.UserDTO;
import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;

    // ===================== CREATE =====================

    @Transactional
    public UserDTO createStudent(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (studentRepository.existsByRollNumber(request.getRollNumber())) {
            throw new RuntimeException("Roll number already exists");
        }

        // Generate password: Student@ + last 4 digits
        String generatedPassword = "Student@" + request.getUsername().substring(
                Math.max(0, request.getUsername().length() - 4)
        );

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.getRoles().add(Role.STUDENT);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Student student = new Student();
        student.setUser(savedUser);
        student.setRollNumber(request.getRollNumber());
        student.setSemester(request.getSemester());
        student.setStream(Student.Stream.valueOf(request.getStream()));
        student.setSection(request.getSection());
        student.setAcademicYear("2024-2025");

        Student savedStudent = studentRepository.save(student);

        // ✅ Convert with student entity, not just user
        UserDTO dto = convertStudentToUserDTO(savedStudent);
        dto.setGeneratedPassword(generatedPassword); // Include password for display
        return dto;
    }

//    @Transactional
//    public UserDTO createFaculty(CreateUserRequest request) {
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        if (facultyRepository.existsByEmployeeId(request.getEmployeeId())) {
//            throw new RuntimeException("Employee ID already exists");
//        }
//
//        // Generate password: Faculty@ + last 4 digits
//        String generatedPassword = "Faculty@" + request.getUsername().substring(
//                Math.max(0, request.getUsername().length() - 4)
//        );
//
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(passwordEncoder.encode(generatedPassword));
//        user.setEmail(request.getEmail());
//        user.setFullName(request.getFullName());
//        user.getRoles().add(Role.FACULTY);
//        user.setEnabled(true);
//        user.setCreatedAt(LocalDateTime.now());
//
//        User savedUser = userRepository.save(user);
//
//        Faculty faculty = new Faculty();
//        faculty.setUser(savedUser);
//        faculty.setEmployeeId(request.getEmployeeId());
//        faculty.setDepartment(request.getDepartment());
//        faculty.setDesignation(request.getDesignation());
//
//        Faculty savedFaculty = facultyRepository.save(faculty);
//
//        // ✅ Convert with faculty entity, not just user
//        UserDTO dto = convertFacultyToUserDTO(savedFaculty);
//        dto.setGeneratedPassword(generatedPassword); // Include password for display
//        return dto;
//    }

    @Transactional
    public UserDTO createFaculty(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (facultyRepository.existsByEmployeeId(request.getEmployeeId())) {
            throw new RuntimeException("Employee ID already exists");
        }

        // Generate password: Faculty@ + last 4 digits
        String generatedPassword = "Faculty@" + request.getUsername().substring(
                Math.max(0, request.getUsername().length() - 4)
        );

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.getRoles().add(Role.FACULTY);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        Faculty faculty = new Faculty();
        faculty.setUser(savedUser);
        faculty.setEmployeeId(request.getEmployeeId());
        faculty.setDepartment(request.getDepartment());
        faculty.setDesignation(request.getDesignation());
        faculty.setPhoneNumber(request.getPhoneNumber());  // ✅ ADD THIS LINE

        Faculty savedFaculty = facultyRepository.save(faculty);

        // ✅ Convert with faculty entity, not just user
        UserDTO dto = convertFacultyToUserDTO(savedFaculty);
        dto.setGeneratedPassword(generatedPassword); // Include password for display
        return dto;
    }

    @Transactional
    public UserDTO createAdmin(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        String generatedPassword = "Admin@" + request.getUsername().substring(
                Math.max(0, request.getUsername().length() - 4)
        );

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(generatedPassword));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.getRoles().add(Role.ADMIN);
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        UserDTO dto = convertToUserDTO(savedUser);
        dto.setGeneratedPassword(generatedPassword);
        return dto;
    }

    // ===================== READ =====================

    @Transactional(readOnly = true)
    public List<UserDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::convertStudentToUserDTO) // ✅ Use student converter
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllFaculty() {
        return facultyRepository.findAll()
                .stream()
                .map(this::convertFacultyToUserDTO) // ✅ Use faculty converter
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllAdmins() {
        return userRepository.findByRolesContaining(Role.ADMIN)
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserDTO(user);
    }

    // ===================== UPDATE =====================

    @Transactional
    public UserDTO updateUser(Long userId, CreateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);

        if (user.getRoles().contains(Role.STUDENT)) {
            studentRepository.findByUserId(userId).ifPresent(student -> {
                student.setSemester(request.getSemester());
                student.setSection(request.getSection());
                studentRepository.save(student);
            });
        } else if (user.getRoles().contains(Role.FACULTY)) {
            facultyRepository.findByUserId(userId).ifPresent(faculty -> {
                faculty.setDepartment(request.getDepartment());
                faculty.setDesignation(request.getDesignation());
                facultyRepository.save(faculty);
            });
        }

        return convertToUserDTO(updatedUser);
    }

    // ===================== STATUS =====================

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
    }

    // ===================== DASHBOARD =====================

    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalStudents(studentRepository.count());
        stats.setTotalFaculty(facultyRepository.count());
        stats.setTotalAdmins(userRepository.countByRolesContaining(Role.ADMIN));
        stats.setActiveUsers(userRepository.countByEnabledTrue());
        stats.setTotalSubjects(subjectRepository.count());
        stats.setActiveEnrollments(enrollmentRepository.count());
        return stats;
    }

    // ===================== MAPPERS =====================

    /**
     * ✅ CRITICAL: Convert Student entity to UserDTO
     * Sets dto.id = student.id (NOT user.id)
     */
    private UserDTO convertStudentToUserDTO(Student student) {
        UserDTO dto = new UserDTO();

        // ✅ CRITICAL FIX: Use student.id, not user.id
        dto.setId(student.getId()); // This is student.id
        dto.setUserId(student.getUser().getId()); // This is user.id

        // User fields
        dto.setUsername(student.getUser().getUsername());
        dto.setEmail(student.getUser().getEmail());
        dto.setFullName(student.getUser().getFullName());
        dto.setRole(Role.STUDENT.name());
        dto.setIsActive(student.getUser().getEnabled());
        dto.setCreatedAt(student.getUser().getCreatedAt());

        // Student fields
        dto.setRollNumber(student.getRollNumber());
        dto.setSemester(student.getSemester());
        dto.setStream(student.getStream().name());
        dto.setSection(student.getSection());
        dto.setAcademicYear(student.getAcademicYear());

        return dto;
    }

    /**
     * ✅ CRITICAL: Convert Faculty entity to UserDTO
     * Sets dto.id = faculty.id (NOT user.id)
     */
    private UserDTO convertFacultyToUserDTO(Faculty faculty) {
        UserDTO dto = new UserDTO();

        // ✅ CRITICAL FIX: Use faculty.id, not user.id
        dto.setId(faculty.getId()); // This is faculty.id
        dto.setUserId(faculty.getUser().getId()); // This is user.id

        // User fields
        dto.setUsername(faculty.getUser().getUsername());
        dto.setEmail(faculty.getUser().getEmail());
        dto.setFullName(faculty.getUser().getFullName());
        dto.setRole(Role.FACULTY.name());
        dto.setIsActive(faculty.getUser().getEnabled());
        dto.setCreatedAt(faculty.getUser().getCreatedAt());

        // Faculty fields
        dto.setEmployeeId(faculty.getEmployeeId());
        dto.setDepartment(faculty.getDepartment());
        dto.setDesignation(faculty.getDesignation());

        return dto;
    }

    /**
     * Generic converter for users (used for admins)
     */
    private UserDTO convertToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setIsActive(user.getEnabled());
        dto.setCreatedAt(user.getCreatedAt());

        Role primaryRole = user.getPrimaryRole();
        if (primaryRole != null) {
            dto.setRole(primaryRole.name());
        }

        return dto;
    }

    // ===================== INNER DTO =====================

    @lombok.Data
    public static class DashboardStats {
        private Long totalStudents;
        private Long totalFaculty;
        private Long totalAdmins;
        private Long activeUsers;
        private Long totalSubjects;
        private Long activeEnrollments;
    }
}
