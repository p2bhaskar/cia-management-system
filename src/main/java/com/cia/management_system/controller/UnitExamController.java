//package com.cia.management_system.controller;
//
//import com.cia.management_system.dto.*;
//import com.cia.management_system.service.GoogleSheetsService;
//import com.cia.management_system.service.UnitExamService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/exams")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class UnitExamController {
//
//    private final UnitExamService examService;
//    private final GoogleSheetsService googleSheetsService;
//
//    // ==================== FACULTY ENDPOINTS ====================
//
//    /**
//     * Create new unit exam
//     * POST /api/exams
//     */
//    @PostMapping
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<UnitExamDTO> createExam(
//            @Valid @RequestBody CreateUnitExamRequest request,
//            Authentication authentication) {
//        try {
//            UnitExamDTO exam = examService.createExam(request, authentication.getName());
//            return ResponseEntity.status(HttpStatus.CREATED).body(exam);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    /**
//     * Get exams by subject
//     * GET /api/exams/subject/{subjectId}
//     */
//    @GetMapping("/subject/{subjectId}")
//    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
//    public ResponseEntity<List<UnitExamDTO>> getExamsBySubject(@PathVariable Long subjectId) {
//        List<UnitExamDTO> exams = examService.getExamsBySubject(subjectId);
//        return ResponseEntity.ok(exams);
//    }
//
//    /**
//     * Publish exam (make visible to students)
//     * PUT /api/exams/{examId}/publish
//     */
//    @PutMapping("/{examId}/publish")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<UnitExamDTO> publishExam(@PathVariable Long examId) {
//        try {
//            UnitExamDTO exam = examService.publishExam(examId);
//            return ResponseEntity.ok(exam);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * Unpublish exam
//     * PUT /api/exams/{examId}/unpublish
//     */
//    @PutMapping("/{examId}/unpublish")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<UnitExamDTO> unpublishExam(@PathVariable Long examId) {
//        try {
//            UnitExamDTO exam = examService.unpublishExam(examId);
//            return ResponseEntity.ok(exam);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * Import scores from Google Sheets
//     * POST /api/exams/{examId}/import-scores
//     */
//    @PostMapping("/{examId}/import-scores")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<GoogleSheetsService.ScoreImportResult> importScores(
//            @PathVariable Long examId,
//            @Valid @RequestBody ImportScoresRequest request) {
//        try {
//            GoogleSheetsService.ScoreImportResult result =
//                    googleSheetsService.importScoresFromSheet(
//                            examId,
//                            request.getGoogleSheetId(),
//                            request.getRange()
//                    );
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            GoogleSheetsService.ScoreImportResult errorResult =
//                    new GoogleSheetsService.ScoreImportResult();
//            errorResult.setSuccess(false);
//            errorResult.setMessage(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
//        }
//    }
//
//    /**
//     * Get all attempts for an exam
//     * GET /api/exams/{examId}/attempts
//     */
//    @GetMapping("/{examId}/attempts")
//    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
//    public ResponseEntity<List<ExamAttemptDTO>> getAttemptsByExam(@PathVariable Long examId) {
//        List<ExamAttemptDTO> attempts = examService.getAttemptsByExam(examId);
//        return ResponseEntity.ok(attempts);
//    }
//
//    /**
//     * Update exam score manually
//     * PUT /api/exams/attempts/{attemptId}/score
//     */
//    @PutMapping("/attempts/{attemptId}/score")
//    @PreAuthorize("hasRole('FACULTY')")
//    public ResponseEntity<Void> updateExamScore(
//            @PathVariable Long attemptId,
//            @RequestBody UpdateScoreRequest request) {
//        try {
//            examService.updateExamScore(attemptId, request.getMarks(), request.getRemarks());
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // ==================== STUDENT ENDPOINTS ====================
//
//    /**
//     * Get exams for student (with unlock status)
//     * GET /api/exams/student/subject/{subjectId}
//     */
//    @GetMapping("/student/subject/{subjectId}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<List<UnitExamDTO>> getExamsForStudent(
//            @PathVariable Long subjectId,
//            Authentication authentication) {
//        // TODO: Get student ID from authentication
//        // For now, return empty list
//        return ResponseEntity.ok(List.of());
//    }
//
//    /**
//     * Start exam (create attempt)
//     * POST /api/exams/{examId}/start
//     */
//    @PostMapping("/{examId}/start")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<ExamAttemptDTO> startExam(
//            @PathVariable Long examId,
//            Authentication authentication) {
//        try {
//            // TODO: Get student ID from authentication
//            // For now, throw error
//            throw new RuntimeException("Student ID not found in authentication");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    /**
//     * Mark exam as complete (after submission in Google Forms)
//     * PUT /api/exams/attempts/{attemptId}/complete
//     */
//    @PutMapping("/attempts/{attemptId}/complete")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<ExamAttemptDTO> markExamComplete(@PathVariable Long attemptId) {
//        try {
//            ExamAttemptDTO attempt = examService.markExamComplete(attemptId);
//            return ResponseEntity.ok(attempt);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    /**
//     * DTO for updating score
//     */
//    @lombok.Data
//    static class UpdateScoreRequest {
//        private Double marks;
//        private String remarks;
//    }
//}


package com.cia.management_system.controller;

import com.cia.management_system.dto.*;
import com.cia.management_system.model.Student;
import com.cia.management_system.repository.StudentRepository;
import com.cia.management_system.service.GoogleSheetsService;
import com.cia.management_system.service.UnitExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UnitExamController {

    private final UnitExamService examService;
    private final GoogleSheetsService googleSheetsService;
    private final StudentRepository studentRepository;

    // ==================== FACULTY ENDPOINTS ====================

    /**
     * Create new unit exam
     * POST /api/exams
     */
    @PostMapping
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<UnitExamDTO> createExam(
            @Valid @RequestBody CreateUnitExamRequest request,
            Authentication authentication) {
        try {
            UnitExamDTO exam = examService.createExam(request, authentication.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(exam);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get exams by subject
     * GET /api/exams/subject/{subjectId}
     */
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<List<UnitExamDTO>> getExamsBySubject(@PathVariable Long subjectId) {
        List<UnitExamDTO> exams = examService.getExamsBySubject(subjectId);
        return ResponseEntity.ok(exams);
    }

    /**
     * Publish exam (make visible to students)
     * PUT /api/exams/{examId}/publish
     */
    @PutMapping("/{examId}/publish")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<UnitExamDTO> publishExam(@PathVariable Long examId) {
        try {
            UnitExamDTO exam = examService.publishExam(examId);
            return ResponseEntity.ok(exam);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Unpublish exam
     * PUT /api/exams/{examId}/unpublish
     */
    @PutMapping("/{examId}/unpublish")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<UnitExamDTO> unpublishExam(@PathVariable Long examId) {
        try {
            UnitExamDTO exam = examService.unpublishExam(examId);
            return ResponseEntity.ok(exam);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Import scores from Google Sheets
     * POST /api/exams/{examId}/import-scores
     */
    @PostMapping("/{examId}/import-scores")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<GoogleSheetsService.ScoreImportResult> importScores(
            @PathVariable Long examId,
            @Valid @RequestBody ImportScoresRequest request) {
        try {
            GoogleSheetsService.ScoreImportResult result =
                    googleSheetsService.importScoresFromSheet(
                            examId,
                            request.getGoogleSheetId(),
                            request.getRange()
                    );
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            GoogleSheetsService.ScoreImportResult errorResult =
                    new GoogleSheetsService.ScoreImportResult();
            errorResult.setSuccess(false);
            errorResult.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
    }

    /**
     * Get all attempts for an exam
     * GET /api/exams/{examId}/attempts
     */
    @GetMapping("/{examId}/attempts")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<List<ExamAttemptDTO>> getAttemptsByExam(@PathVariable Long examId) {
        List<ExamAttemptDTO> attempts = examService.getAttemptsByExam(examId);
        return ResponseEntity.ok(attempts);
    }

    /**
     * Update exam score manually
     * PUT /api/exams/attempts/{attemptId}/score
     */
    @PutMapping("/attempts/{attemptId}/score")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<Void> updateExamScore(
            @PathVariable Long attemptId,
            @RequestBody UpdateScoreRequest request) {
        try {
            examService.updateExamScore(attemptId, request.getMarks(), request.getRemarks());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ==================== STUDENT ENDPOINTS ====================

    /**
     * Get exams for student (with unlock status)
     * GET /api/exams/student/subject/{subjectId}
     */
    @GetMapping("/student/subject/{subjectId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<UnitExamDTO>> getExamsForStudent(
            @PathVariable Long subjectId,
            Authentication authentication) {
        try {
            // Get student from authentication
            Student student = studentRepository.findByUserUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            List<UnitExamDTO> exams = examService.getExamsForStudent(student.getId(), subjectId);
            return ResponseEntity.ok(exams);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Start exam (create attempt)
     * POST /api/exams/{examId}/start
     */
    @PostMapping("/{examId}/start")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ExamAttemptDTO> startExam(
            @PathVariable Long examId,
            Authentication authentication) {
        try {
            // Get student from authentication
            Student student = studentRepository.findByUserUsername(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            ExamAttemptDTO attempt = examService.startExam(examId, student.getId());
            return ResponseEntity.ok(attempt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mark exam as complete (after submission in Google Forms)
     * PUT /api/exams/attempts/{attemptId}/complete
     */
    @PutMapping("/attempts/{attemptId}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ExamAttemptDTO> markExamComplete(@PathVariable Long attemptId) {
        try {
            ExamAttemptDTO attempt = examService.markExamComplete(attemptId);
            return ResponseEntity.ok(attempt);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DTO for updating score
     */
    @lombok.Data
    static class UpdateScoreRequest {
        private Double marks;
        private String remarks;
    }
}