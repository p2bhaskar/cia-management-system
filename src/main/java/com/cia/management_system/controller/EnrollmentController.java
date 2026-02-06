//package com.cia.management_system.controller;
//
//
//import com.cia.management_system.dto.EnrollmentDTO;
//import com.cia.management_system.service.EnrollmentService;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/enrollments")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class EnrollmentController {
//
//    private final EnrollmentService enrollmentService;
//
//    // Admin: Enroll single student
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<EnrollmentDTO> enrollStudent(@RequestBody EnrollmentRequest request) {
//        try {
//            EnrollmentDTO enrollment = enrollmentService.enrollStudent(
//                    request.getStudentId(),
//                    request.getSubjectId(),
//                    request.getFacultyId(),
//                    request.getAcademicYear()
//            );
//            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Admin: Bulk enroll students
//    @PostMapping("/bulk")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> bulkEnrollStudents(@RequestBody BulkEnrollmentRequest request) {
//        try {
//            enrollmentService.bulkEnrollStudents(
//                    request.getStudentIds(),
//                    request.getSubjectId(),
//                    request.getFacultyId(),
//                    request.getAcademicYear()
//            );
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Get all enrollments
//    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
//        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
//        return ResponseEntity.ok(enrollments);
//    }
//
//    // Get enrollments by student
//    @GetMapping("/student/{studentId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
//    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudent(@PathVariable Long studentId) {
//        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
//        return ResponseEntity.ok(enrollments);
//    }
//
//    // Get enrollments by subject
//    @GetMapping("/subject/{subjectId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
//    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsBySubject(@PathVariable Long subjectId) {
//        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsBySubject(subjectId);
//        return ResponseEntity.ok(enrollments);
//    }
//
//    // Get enrollments by faculty
//    @GetMapping("/faculty/{facultyId}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
//    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByFaculty(@PathVariable Long facultyId) {
//        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByFaculty(facultyId);
//        return ResponseEntity.ok(enrollments);
//    }
//
//    // Remove enrollment
//    @DeleteMapping("/{enrollmentId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> removeEnrollment(@PathVariable Long enrollmentId) {
//        try {
//            enrollmentService.removeEnrollment(enrollmentId);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @Data
//    static class EnrollmentRequest {
//        private Long studentId;
//        private Long subjectId;
//        private Long facultyId;
//        private String academicYear;
//    }
//
//    @Data
//    static class BulkEnrollmentRequest {
//        private List<Long> studentIds;
//        private Long subjectId;
//        private Long facultyId;
//        private String academicYear;
//    }
//}


package com.cia.management_system.controller;

import com.cia.management_system.dto.BulkImportResult;
import com.cia.management_system.dto.EnrollmentDTO;
import com.cia.management_system.service.BulkEnrollmentService;
import com.cia.management_system.service.EnrollmentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final BulkEnrollmentService bulkEnrollmentService;

    // Admin: Enroll single student
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnrollmentDTO> enrollStudent(@RequestBody EnrollmentRequest request) {
        try {
            EnrollmentDTO enrollment = enrollmentService.enrollStudent(
                    request.getStudentId(),
                    request.getSubjectId(),
                    request.getFacultyId(),
                    request.getAcademicYear()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Admin: Bulk enroll students (same subject/faculty)
    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> bulkEnrollStudents(@RequestBody BulkEnrollmentRequest request) {
        try {
            enrollmentService.bulkEnrollStudents(
                    request.getStudentIds(),
                    request.getSubjectId(),
                    request.getFacultyId(),
                    request.getAcademicYear()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("successCount", request.getStudentIds().size());
            response.put("message", "Successfully enrolled " + request.getStudentIds().size() + " students");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ NEW: Admin: Bulk import enrollments from Excel
    @PostMapping("/bulk-import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> bulkImportEnrollments(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            BulkImportResult result = bulkEnrollmentService.importEnrollments(file);

            Map<String, Object> response = new HashMap<>();
            response.put("totalRecords", result.getTotalRecords());
            response.put("successCount", result.getSuccessCount());
            response.put("failureCount", result.getFailureCount());
            response.put("errors", result.getErrors());
            response.put("warnings", result.getWarnings());
            response.put("message", result.getMessage());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to import: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ✅ NEW: Download enrollment template
    @GetMapping("/template")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> downloadEnrollmentTemplate() {
        String template = "Roll Number,Subject Code,Employee ID,Academic Year\n" +
                "250213000001,BCA101,FAC001,2024-2025\n" +
                "250213000002,BCA101,FAC001,2024-2025\n" +
                "250205000001,MCA101,FAC002,2024-2025";
        return ResponseEntity.ok(template);
    }

    // Get all enrollments
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    // Get enrollments by student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(enrollments);
    }

    // Get enrollments by subject
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsBySubject(@PathVariable Long subjectId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsBySubject(subjectId);
        return ResponseEntity.ok(enrollments);
    }

    // Get enrollments by faculty
    @GetMapping("/faculty/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByFaculty(@PathVariable Long facultyId) {
        List<EnrollmentDTO> enrollments = enrollmentService.getEnrollmentsByFaculty(facultyId);
        return ResponseEntity.ok(enrollments);
    }

    // Remove enrollment
    @DeleteMapping("/{enrollmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeEnrollment(@PathVariable Long enrollmentId) {
        try {
            enrollmentService.removeEnrollment(enrollmentId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    static class EnrollmentRequest {
        private Long studentId;
        private Long subjectId;
        private Long facultyId;
        private String academicYear;
    }

    @Data
    static class BulkEnrollmentRequest {
        private List<Long> studentIds;
        private Long subjectId;
        private Long facultyId;
        private String academicYear;
    }
}