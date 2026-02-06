//package com.cia.management_system.controller;
//
//
//
//import com.cia.management_system.model.FacultySubjectAssignment;
//import com.cia.management_system.service.FacultySubjectAssignmentService;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/faculty-subject-assignments")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class FacultySubjectAssignmentController {
//
//    private final FacultySubjectAssignmentService assignmentService;
//
//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<FacultySubjectAssignment> assignSubject(
//            @RequestBody AssignmentRequest request,
//            Authentication authentication) {
//        try {
//            FacultySubjectAssignment assignment = assignmentService.assignSubjectToFaculty(
//                    request.getFacultyId(),
//                    request.getSubjectId(),
//                    request.getAcademicYear(),
//                    request.getSection(),
//                    authentication.getName()
//            );
//            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/faculty/{facultyId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<FacultySubjectAssignment>> getAssignmentsByFaculty(
//            @PathVariable Long facultyId) {
//        List<FacultySubjectAssignment> assignments = assignmentService.getAssignmentsByFaculty(facultyId);
//        return ResponseEntity.ok(assignments);
//    }
//
//    @GetMapping("/subject/{subjectId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<FacultySubjectAssignment>> getAssignmentsBySubject(
//            @PathVariable Long subjectId) {
//        List<FacultySubjectAssignment> assignments = assignmentService.getAssignmentsBySubject(subjectId);
//        return ResponseEntity.ok(assignments);
//    }
//
//    @DeleteMapping("/{assignmentId}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> removeAssignment(@PathVariable Long assignmentId) {
//        try {
//            assignmentService.removeAssignment(assignmentId);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @Data
//    static class AssignmentRequest {
//        private Long facultyId;
//        private Long subjectId;
//        private String academicYear;
//        private String section;
//    }
//}

package com.cia.management_system.controller;

import com.cia.management_system.dto.AssignSubjectRequest;
import com.cia.management_system.dto.FacultySubjectAssignmentDTO;
import com.cia.management_system.service.FacultySubjectAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculty-subject-assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacultySubjectAssignmentController {

    private final FacultySubjectAssignmentService assignmentService;

    /**
     * Assign subject to faculty
     * POST /api/faculty-subject-assignments
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignSubject(
            @Valid @RequestBody AssignSubjectRequest request,
            Authentication authentication) {
        try {
            FacultySubjectAssignmentDTO assignment =
                    assignmentService.assignSubject(request, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get assignments by faculty
     * GET /api/faculty-subject-assignments/faculty/{facultyId}
     */
    @GetMapping("/faculty/{facultyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<FacultySubjectAssignmentDTO>> getAssignmentsByFaculty(
            @PathVariable Long facultyId) {
        try {
            List<FacultySubjectAssignmentDTO> assignments =
                    assignmentService.getAssignmentsByFaculty(facultyId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get assignments by subject
     * GET /api/faculty-subject-assignments/subject/{subjectId}
     */
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<FacultySubjectAssignmentDTO>> getAssignmentsBySubject(
            @PathVariable Long subjectId) {
        try {
            List<FacultySubjectAssignmentDTO> assignments =
                    assignmentService.getAssignmentsBySubject(subjectId);
            return ResponseEntity.ok(assignments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Remove assignment (soft delete)
     * DELETE /api/faculty-subject-assignments/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeAssignment(@PathVariable Long id) {
        try {
            assignmentService.removeAssignment(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Assignment removed successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
