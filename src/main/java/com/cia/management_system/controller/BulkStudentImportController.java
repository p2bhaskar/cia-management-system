//package com.cia.management_system.controller;
//
//
//
//import com.cia.management_system.dto.BulkStudentImportResponse;
//import com.cia.management_system.service.BulkStudentImportService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/admin/students")
//@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
//public class BulkStudentImportController {
//
//    private final BulkStudentImportService bulkStudentImportService;
//
//    /**
//     * Download Excel template for bulk student import
//     * GET /api/admin/students/import/template
//     */
//    @GetMapping("/import/template")
//    public ResponseEntity<byte[]> downloadTemplate() {
//        try {
//            byte[] template = bulkStudentImportService.generateTemplate();
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment", "student_import_template.xlsx");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(template);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    /**
//     * Import students from Excel file
//     * POST /api/admin/students/import
//     */
//    @PostMapping("/import")
//    public ResponseEntity<BulkStudentImportResponse> importStudents(
//            @RequestParam("file") MultipartFile file) {
//
//        // Validate file
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        String filename = file.getOriginalFilename();
//        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        try {
//            BulkStudentImportResponse response = bulkStudentImportService.importStudentsFromExcel(file);
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    /**
//     * Download credentials Excel file
//     * POST /api/admin/students/credentials/download
//     */
//    @PostMapping("/credentials/download")
//    public ResponseEntity<byte[]> downloadCredentials(
//            @RequestBody BulkStudentImportResponse importResponse) {
//
//        try {
//            byte[] credentialsExcel = bulkStudentImportService.generateCredentialsExcel(
//                    importResponse.getCredentials()
//            );
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment",
//                    "student_credentials_" + System.currentTimeMillis() + ".xlsx");
//
//            return ResponseEntity.ok()
//                    .headers(headers)
//                    .body(credentialsExcel);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}