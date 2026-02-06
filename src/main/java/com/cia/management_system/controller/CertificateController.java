package com.cia.management_system.controller;

import com.cia.management_system.dto.*;
import com.cia.management_system.service.CertificateService;
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
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CertificateController {

    private final CertificateService certificateService;

    // ==================== STUDENT ENDPOINTS ====================

    /**
     * Upload certificate
     * POST /api/certificates
     */
    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> uploadCertificate(
            @Valid @RequestBody UploadCertificateRequest request,
            Authentication authentication) {
        try {
            CertificateDTO certificate = certificateService.uploadCertificate(
                    request,
                    authentication.getName()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get my certificates
     * GET /api/certificates/my-certificates
     */
    @GetMapping("/my-certificates")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CertificateDTO>> getMyCertificates(Authentication authentication) {
        List<CertificateDTO> certificates = certificateService.getMyCertificates(
                authentication.getName()
        );
        return ResponseEntity.ok(certificates);
    }

    /**
     * Get my certificates by subject
     * GET /api/certificates/my-certificates/subject/{subjectId}
     */
    @GetMapping("/my-certificates/subject/{subjectId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CertificateDTO>> getMyCertificatesBySubject(
            @PathVariable Long subjectId,
            Authentication authentication) {
        List<CertificateDTO> certificates = certificateService.getMyCertificatesBySubject(
                subjectId,
                authentication.getName()
        );
        return ResponseEntity.ok(certificates);
    }

    /**
     * Get my activity marks
     * GET /api/certificates/my-activity-marks
     */
    @GetMapping("/my-activity-marks")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ActivityMarksDTO>> getMyActivityMarks(Authentication authentication) {
        // This will be implemented by getting student ID from authentication
        // For now, return empty list
        return ResponseEntity.ok(List.of());
    }

    // ==================== FACULTY ENDPOINTS ====================

    /**
     * Get pending certificates for review
     * GET /api/certificates/pending/subject/{subjectId}
     */
    @GetMapping("/pending/subject/{subjectId}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<CertificateDTO>> getPendingCertificates(@PathVariable Long subjectId) {
        List<CertificateDTO> certificates = certificateService.getPendingCertificates(subjectId);
        return ResponseEntity.ok(certificates);
    }

    /**
     * Get all certificates by subject
     * GET /api/certificates/subject/{subjectId}
     */
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<CertificateDTO>> getAllCertificatesBySubject(@PathVariable Long subjectId) {
        List<CertificateDTO> certificates = certificateService.getAllCertificatesBySubject(subjectId);
        return ResponseEntity.ok(certificates);
    }

    /**
     * Review certificate (approve/reject)
     * PUT /api/certificates/{certificateId}/review
     */
    @PutMapping("/{certificateId}/review")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> reviewCertificate(
            @PathVariable Long certificateId,
            @Valid @RequestBody ReviewCertificateRequest request,
            Authentication authentication) {
        try {
            CertificateDTO certificate = certificateService.reviewCertificate(
                    certificateId,
                    request,
                    authentication.getName()
            );
            return ResponseEntity.ok(certificate);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get activity marks for a student in a subject
     * GET /api/certificates/activity-marks/student/{studentId}/subject/{subjectId}
     */
    @GetMapping("/activity-marks/student/{studentId}/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<ActivityMarksDTO> getActivityMarks(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {
        try {
            ActivityMarksDTO marks = certificateService.getActivityMarks(studentId, subjectId);
            return ResponseEntity.ok(marks);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}