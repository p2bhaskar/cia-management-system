package com.cia.management_system.controller;



import com.cia.management_system.dto.AttendanceSummaryDTO;
import com.cia.management_system.service.AttendanceService;
import com.cia.management_system.service.ExcelParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // Faculty: Upload attendance Excel
    @PostMapping("/upload/{subjectId}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ExcelParserService.AttendanceParseResult> uploadAttendance(
            @PathVariable Long subjectId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            ExcelParserService.AttendanceParseResult result =
                    attendanceService.uploadAttendanceExcel(file, subjectId, authentication.getName());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ExcelParserService.AttendanceParseResult errorResult =
                    new ExcelParserService.AttendanceParseResult();
            errorResult.setSuccess(false);
            errorResult.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResult);
        }
    }

    // Get attendance by subject
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<List<AttendanceSummaryDTO>> getAttendanceBySubject(@PathVariable Long subjectId) {
        List<AttendanceSummaryDTO> attendance = attendanceService.getAttendanceBySubject(subjectId);
        return ResponseEntity.ok(attendance);
    }

    // Student: Get my attendance
    @GetMapping("/my-attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AttendanceSummaryDTO>> getMyAttendance(Authentication authentication) {
        // TODO: Get student ID from authentication
        // For now, returning empty list
        return ResponseEntity.ok(List.of());
    }

    // Get attendance for specific student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'STUDENT')")
    public ResponseEntity<List<AttendanceSummaryDTO>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<AttendanceSummaryDTO> attendance = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendance);
    }
}
