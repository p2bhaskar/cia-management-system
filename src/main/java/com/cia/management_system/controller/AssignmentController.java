package com.cia.management_system.controller;



//import com.cia.management_system.dto.*;
//import com.cia.management_system.service.AssignmentService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/assignments")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class AssignmentController {
//
//    private final AssignmentService assignmentService;
//
//    // Faculty: Create assignment
//    @PostMapping
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<AssignmentDTO> createAssignment(
//            @Valid @RequestBody CreateAssignmentRequest request,
//            Authentication authentication) {
//        try {
//            AssignmentDTO assignment = assignmentService.createAssignment(request, authentication.getName());
//            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Get assignments by subject (accessible to both faculty and students)
//    @GetMapping("/subject/{subjectId}")
//    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
//    public ResponseEntity<List<AssignmentDTO>> getAssignmentsBySubject(@PathVariable Long subjectId) {
//        List<AssignmentDTO> assignments = assignmentService.getAssignmentsBySubject(subjectId);
//        return ResponseEntity.ok(assignments);
//    }
//
//    // Faculty: Get all assignments created by them
//    @GetMapping("/my-assignments")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<List<AssignmentDTO>> getMyAssignments(Authentication authentication) {
//        List<AssignmentDTO> assignments = assignmentService.getAssignmentsByFaculty(authentication.getName());
//        return ResponseEntity.ok(assignments);
//    }
//
//    // Faculty: Mark student as submitted
//    @PostMapping("/{assignmentId}/submissions")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<AssignmentSubmissionDTO> markSubmission(
//            @PathVariable Long assignmentId,
//            @Valid @RequestBody MarkSubmissionRequest request) {
//        try {
//            AssignmentSubmissionDTO submission = assignmentService.markSubmission(assignmentId, request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(submission);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Faculty: Grade submission
//    @PutMapping("/submissions/{submissionId}/grade")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<AssignmentSubmissionDTO> gradeSubmission(
//            @PathVariable Long submissionId,
//            @Valid @RequestBody GradeAssignmentRequest request,
//            Authentication authentication) {
//        try {
//            AssignmentSubmissionDTO submission = assignmentService.gradeSubmission(
//                    submissionId, request, authentication.getName());
//            return ResponseEntity.ok(submission);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Faculty: Get all submissions for an assignment
//    @GetMapping("/{assignmentId}/submissions")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<List<AssignmentSubmissionDTO>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
//        List<AssignmentSubmissionDTO> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
//        return ResponseEntity.ok(submissions);
//    }
//
//    // Student: Get my submissions
//    @GetMapping("/my-submissions")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<List<AssignmentSubmissionDTO>> getMySubmissions(Authentication authentication) {
//        List<AssignmentSubmissionDTO> submissions = assignmentService.getSubmissionsByStudent(authentication.getName());
//        return ResponseEntity.ok(submissions);
//    }
//}





import com.cia.management_system.dto.*;
import com.cia.management_system.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssignmentController {

    private final AssignmentService assignmentService;

    // Faculty: Create assignment
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<AssignmentDTO> createAssignment(
            @Valid @RequestBody CreateAssignmentRequest request,
            Authentication authentication) {
        try {
            AssignmentDTO assignment = assignmentService.createAssignment(request, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get assignments by subject (accessible to both faculty and students)
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'STUDENT')")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsBySubject(@PathVariable Long subjectId) {
        List<AssignmentDTO> assignments = assignmentService.getAssignmentsBySubject(subjectId);
        return ResponseEntity.ok(assignments);
    }

    // Faculty: Get all assignments created by them
    @GetMapping("/my-assignments")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<AssignmentDTO>> getMyAssignments(Authentication authentication) {
        List<AssignmentDTO> assignments = assignmentService.getAssignmentsByFaculty(authentication.getName());
        return ResponseEntity.ok(assignments);
    }

    // NEW: Student: Get assignments based on enrollments
    @GetMapping("/student/my-assignments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AssignmentDTO>> getMyEnrolledAssignments(Authentication authentication) {
        List<AssignmentDTO> assignments = assignmentService.getAssignmentsForStudent(authentication.getName());
        return ResponseEntity.ok(assignments);
    }

    // NEW: Student: Mark own submission
    @PostMapping("/{assignmentId}/student-submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<AssignmentSubmissionDTO> studentSubmit(
            @PathVariable Long assignmentId,
            @RequestBody StudentSubmitRequest request,
            Authentication authentication) {
        try {
            AssignmentSubmissionDTO submission = assignmentService.studentMarkSubmission(
                    assignmentId, authentication.getName(), request.getSubmissionUrl());
            return ResponseEntity.status(HttpStatus.CREATED).body(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Faculty: Mark student as submitted
    @PostMapping("/{assignmentId}/submissions")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<AssignmentSubmissionDTO> markSubmission(
            @PathVariable Long assignmentId,
            @Valid @RequestBody MarkSubmissionRequest request) {
        try {
            AssignmentSubmissionDTO submission = assignmentService.markSubmission(assignmentId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Faculty: Grade submission
    @PutMapping("/submissions/{submissionId}/grade")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<AssignmentSubmissionDTO> gradeSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody GradeAssignmentRequest request,
            Authentication authentication) {
        try {
            AssignmentSubmissionDTO submission = assignmentService.gradeSubmission(
                    submissionId, request, authentication.getName());
            return ResponseEntity.ok(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Faculty: Get all submissions for an assignment
    @GetMapping("/{assignmentId}/submissions")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getSubmissionsByAssignment(@PathVariable Long assignmentId) {
        List<AssignmentSubmissionDTO> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    // NEW: Faculty: Get pending submissions (for notifications)
    @GetMapping("/pending-submissions")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getPendingSubmissions(Authentication authentication) {
        List<AssignmentSubmissionDTO> submissions = assignmentService.getPendingSubmissionsForFaculty(authentication.getName());
        return ResponseEntity.ok(submissions);
    }

    // Student: Get my submissions
    @GetMapping("/my-submissions")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AssignmentSubmissionDTO>> getMySubmissions(Authentication authentication) {
        List<AssignmentSubmissionDTO> submissions = assignmentService.getSubmissionsByStudent(authentication.getName());
        return ResponseEntity.ok(submissions);
    }

    @Data
    static class StudentSubmitRequest {
        private String submissionUrl;
    }
}