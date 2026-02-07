//package com.cia.management_system.controller;
//
//
//
//import com.cia.management_system.dto.BulkImportResult;
//import com.cia.management_system.dto.CreateUserRequest;
//import com.cia.management_system.dto.UserDTO;
//import com.cia.management_system.service.AdminService;
//import com.cia.management_system.service.BulkImportService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminController {
//
//    private final AdminService adminService;
//
//    private final BulkImportService bulkImportService; // ✅ ADD THIS
//
//    // Dashboard Stats
//    @GetMapping("/stats")
//    public ResponseEntity<AdminService.DashboardStats> getDashboardStats() {
//        AdminService.DashboardStats stats = adminService.getDashboardStats();
//        return ResponseEntity.ok(stats);
//    }
//
//    // Student Management
//    @PostMapping("/students")
//    public ResponseEntity<UserDTO> createStudent(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createStudent(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/students")
//    public ResponseEntity<List<UserDTO>> getAllStudents() {
//        List<UserDTO> students = adminService.getAllStudents();
//        return ResponseEntity.ok(students);
//    }
//
//    // Faculty Management
//    @PostMapping("/faculty")
//    public ResponseEntity<UserDTO> createFaculty(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createFaculty(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/faculty")
//    public ResponseEntity<List<UserDTO>> getAllFaculty() {
//        List<UserDTO> faculty = adminService.getAllFaculty();
//        return ResponseEntity.ok(faculty);
//    }
//
//    // Admin Management
//    @PostMapping("/admins")
//    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createAdmin(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/admins")
//    public ResponseEntity<List<UserDTO>> getAllAdmins() {
//        List<UserDTO> admins = adminService.getAllAdmins();
//        return ResponseEntity.ok(admins);
//    }
//
//    // User Operations
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
//        try {
//            UserDTO user = adminService.getUserById(userId);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> updateUser(
//            @PathVariable Long userId,
//            @Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.updateUser(userId, request);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/users/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        try {
//            adminService.deleteUser(userId);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/users/{userId}/activate")
//    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
//        try {
//            adminService.activateUser(userId);
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//
//
//
//
//
//
//    // ... existing methods ...
//
//    // ==================== BULK IMPORT ENDPOINTS ====================
//
//    /**
//     * Bulk import students from Excel
//     * POST /api/admin/students/bulk-import
//     */
//    @PostMapping("/students/bulk-import")
//    public ResponseEntity<BulkImportResult> bulkImportStudents(
//            @RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                BulkImportResult result = new BulkImportResult();
//                result.addError("File is empty");
//                return ResponseEntity.badRequest().body(result);
//            }
//
//            BulkImportResult result = bulkImportService.importStudents(file);
//            return ResponseEntity.ok(result);
//
//        } catch (Exception e) {
//            BulkImportResult result = new BulkImportResult();
//            result.addError("Failed to import: " + e.getMessage());
//            return ResponseEntity.badRequest().body(result);
//        }
//    }
//
////    /**
////     * Bulk import faculty from Excel
////     * POST /api/admin/faculty/bulk-import
////     */
////    @PostMapping("/faculty/bulk-import")
////    public ResponseEntity<BulkImportResult> bulkImportFaculty(
////            @RequestParam("file") MultipartFile file) {
////        try {
////            if (file.isEmpty()) {
////                BulkImportResult result = new BulkImportResult();
////                result.addError("File is empty");
////                return ResponseEntity.badRequest().body(result);
////            }
////
////            BulkImportResult result = bulkImportService.importFaculty(file);
////            return ResponseEntity.ok(result);
////
////        } catch (Exception e) {
////            BulkImportResult result = new BulkImportResult();
////            result.addError("Failed to import: " + e.getMessage());
////            return ResponseEntity.badRequest().body(result);
////        }
////    }
////
////    /**
////     * Bulk import subjects from Excel
////     * POST /api/admin/subjects/bulk-import
////     */
////    @PostMapping("/subjects/bulk-import")
////    public ResponseEntity<BulkImportResult> bulkImportSubjects(
////            @RequestParam("file") MultipartFile file) {
////        try {
////            if (file.isEmpty()) {
////                BulkImportResult result = new BulkImportResult();
////                result.addError("File is empty");
////                return ResponseEntity.badRequest().body(result);
////            }
////
////            BulkImportResult result = bulkImportService.importSubjects(file);
////            return ResponseEntity.ok(result);
////
////        } catch (Exception e) {
////            BulkImportResult result = new BulkImportResult();
////            result.addError("Failed to import: " + e.getMessage());
////            return ResponseEntity.badRequest().body(result);
////        }
////    }
//
//    /**
//     * Download sample Excel template for students
//     * GET /api/admin/students/template
//     */
//    @GetMapping("/students/template")
//    public ResponseEntity<String> downloadStudentTemplate() {
//        String template = "Full Name,Email,Roll Number,Semester,Stream,Section,Phone,Address\n" +
//                "John Doe,john@example.com,BCA001,1,BCA,A,9876543210,123 Main St\n" +
//                "Jane Smith,jane@example.com,BCA002,1,BCA,A,9876543211,456 Oak Ave";
//        return ResponseEntity.ok(template);
//    }
//
//    /**
//     * Download sample Excel template for faculty
//     * GET /api/admin/faculty/template
//     */
//    @GetMapping("/faculty/template")
//    public ResponseEntity<String> downloadFacultyTemplate() {
//        String template = "Full Name,Email,Employee ID,Department,Designation,Qualification,Specialization,Phone\n" +
//                "Dr. John Smith,john.smith@college.edu,FAC001,Computer Science,Professor,PhD,AI & ML,9876543210";
//        return ResponseEntity.ok(template);
//    }
//
//    /**
//     * Download sample Excel template for subjects
//     * GET /api/admin/subjects/template
//     */
//    @GetMapping("/subjects/template")
//    public ResponseEntity<String> downloadSubjectTemplate() {
//        String template = "Subject Code,Subject Name,Semester,Stream,Credits,Description\n" +
//                "BCA101,Data Structures,1,BCA,4,Introduction to data structures and algorithms";
//        return ResponseEntity.ok(template);
//    }
//}
//




//package com.cia.management_system.controller;
//
//import com.cia.management_system.dto.BulkImportResult;
//import com.cia.management_system.dto.CreateUserRequest;
//import com.cia.management_system.dto.UserDTO;
//import com.cia.management_system.service.AdminService;
//import com.cia.management_system.service.BulkImportService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminController {
//
//    private final AdminService adminService;
//    private final BulkImportService bulkImportService;
//
//    // ✅ NEW: Store credentials temporarily (in production, use Redis or database)
//    private final Map<String, byte[]> credentialsCache = new HashMap<>();
//
//    // Dashboard Stats
//    @GetMapping("/stats")
//    public ResponseEntity<AdminService.DashboardStats> getDashboardStats() {
//        AdminService.DashboardStats stats = adminService.getDashboardStats();
//        return ResponseEntity.ok(stats);
//    }
//
//    // Student Management
//    @PostMapping("/students")
//    public ResponseEntity<UserDTO> createStudent(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createStudent(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/students")
//    public ResponseEntity<List<UserDTO>> getAllStudents() {
//        List<UserDTO> students = adminService.getAllStudents();
//        return ResponseEntity.ok(students);
//    }
//
//    // Faculty Management
//    @PostMapping("/faculty")
//    public ResponseEntity<UserDTO> createFaculty(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createFaculty(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/faculty")
//    public ResponseEntity<List<UserDTO>> getAllFaculty() {
//        List<UserDTO> faculty = adminService.getAllFaculty();
//        return ResponseEntity.ok(faculty);
//    }
//
//    // Admin Management
//    @PostMapping("/admins")
//    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createAdmin(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/admins")
//    public ResponseEntity<List<UserDTO>> getAllAdmins() {
//        List<UserDTO> admins = adminService.getAllAdmins();
//        return ResponseEntity.ok(admins);
//    }
//
//    // User Operations
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
//        try {
//            UserDTO user = adminService.getUserById(userId);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> updateUser(
//            @PathVariable Long userId,
//            @Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.updateUser(userId, request);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/users/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        try {
//            adminService.deleteUser(userId);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/users/{userId}/activate")
//    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
//        try {
//            adminService.activateUser(userId);
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // ==================== BULK IMPORT ENDPOINTS ====================
//
//    /**
//     * Bulk import students from Excel
//     * POST /api/admin/students/bulk-import
//     */
//    @PostMapping("/students/bulk-import")
//    public ResponseEntity<Map<String, Object>> bulkImportStudents(
//            @RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", false);
//                response.put("message", "File is empty");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            BulkImportResult result = bulkImportService.importStudents(file);
//
//            // ✅ NEW: Store credentials file if available
//            String credentialsKey = null;
//            if (result.isHasCredentialsFile()) {
//                credentialsKey = "credentials_" + System.currentTimeMillis();
//                credentialsCache.put(credentialsKey, result.getCredentialsFile());
//            }
//
//            // ✅ NEW: Return response with credentials download key
//            Map<String, Object> response = new HashMap<>();
//            response.put("totalRecords", result.getTotalRecords());
//            response.put("successCount", result.getSuccessCount());
//            response.put("failureCount", result.getFailureCount());
//            response.put("errors", result.getErrors());
//            response.put("warnings", result.getWarnings());
//            response.put("message", result.getMessage());
//            response.put("hasCredentialsFile", result.isHasCredentialsFile());
//            response.put("credentialsDownloadKey", credentialsKey);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "Failed to import: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    /**
//     * ✅ NEW: Download credentials Excel file
//     * GET /api/admin/students/credentials/{key}
//     */
//    @GetMapping("/students/credentials/{key}")
//    public ResponseEntity<byte[]> downloadCredentials(@PathVariable String key) {
//        byte[] credentialsFile = credentialsCache.get(key);
//
//        if (credentialsFile == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Remove from cache after download (one-time download)
//        credentialsCache.remove(key);
//
//        String filename = "Student_Credentials_" +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
//                ".xlsx";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", filename);
//        headers.setContentLength(credentialsFile.length);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(credentialsFile);
//    }
//
//    /**
//     * Download sample Excel template for students
//     * GET /api/admin/students/template
//     */
//    @GetMapping("/students/template")
//    public ResponseEntity<String> downloadStudentTemplate() {
//        String template = "Full Name,Email,Roll Number,Semester,Stream,Section,Phone,Address\n" +
//                "John Doe,john@example.com,BCA001,1,BCA,A,9876543210,123 Main St\n" +
//                "Jane Smith,jane@example.com,BCA002,1,BCA,A,9876543211,456 Oak Ave";
//        return ResponseEntity.ok(template);
//    }
//
//    /**
//     * Download sample Excel template for faculty
//     * GET /api/admin/faculty/template
//     */
//    @GetMapping("/faculty/template")
//    public ResponseEntity<String> downloadFacultyTemplate() {
//        String template = "Full Name,Email,Employee ID,Department,Designation,Qualification,Specialization,Phone\n" +
//                "Dr. John Smith,john.smith@college.edu,FAC001,Computer Science,Professor,PhD,AI & ML,9876543210";
//        return ResponseEntity.ok(template);
//    }
//
//    /**
//     * Download sample Excel template for subjects
//     * GET /api/admin/subjects/template
//     */
//    @GetMapping("/subjects/template")
//    public ResponseEntity<String> downloadSubjectTemplate() {
//        String template = "Subject Code,Subject Name,Semester,Stream,Credits,Description\n" +
//                "BCA101,Data Structures,1,BCA,4,Introduction to data structures and algorithms";
//        return ResponseEntity.ok(template);
//    }
//}


//bulk import for student faculty subject


//package com.cia.management_system.controller;
//
//import com.cia.management_system.dto.BulkImportResult;
//import com.cia.management_system.dto.CreateUserRequest;
//import com.cia.management_system.dto.UserDTO;
//import com.cia.management_system.service.AdminService;
//import com.cia.management_system.service.BulkImportService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//@PreAuthorize("hasRole('ADMIN')")
//public class AdminController {
//
//    private final AdminService adminService;
//    private final BulkImportService bulkImportService;
//
//    // Store credentials temporarily (in production, use Redis or database)
//    private final Map<String, byte[]> credentialsCache = new HashMap<>();
//
//    // Dashboard Stats
//    @GetMapping("/stats")
//    public ResponseEntity<AdminService.DashboardStats> getDashboardStats() {
//        AdminService.DashboardStats stats = adminService.getDashboardStats();
//        return ResponseEntity.ok(stats);
//    }
//
//    // ==================== STUDENT MANAGEMENT ====================
//
//    @PostMapping("/students")
//    public ResponseEntity<UserDTO> createStudent(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createStudent(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/students")
//    public ResponseEntity<List<UserDTO>> getAllStudents() {
//        List<UserDTO> students = adminService.getAllStudents();
//        return ResponseEntity.ok(students);
//    }
//
//    @PostMapping("/students/bulk-import")
//    public ResponseEntity<Map<String, Object>> bulkImportStudents(
//            @RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", false);
//                response.put("message", "File is empty");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            BulkImportResult result = bulkImportService.importStudents(file);
//
//            String credentialsKey = null;
//            if (result.isHasCredentialsFile()) {
//                credentialsKey = "student_credentials_" + System.currentTimeMillis();
//                credentialsCache.put(credentialsKey, result.getCredentialsFile());
//            }
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("totalRecords", result.getTotalRecords());
//            response.put("successCount", result.getSuccessCount());
//            response.put("failureCount", result.getFailureCount());
//            response.put("errors", result.getErrors());
//            response.put("warnings", result.getWarnings());
//            response.put("message", result.getMessage());
//            response.put("hasCredentialsFile", result.isHasCredentialsFile());
//            response.put("credentialsDownloadKey", credentialsKey);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "Failed to import: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @GetMapping("/students/credentials/{key}")
//    public ResponseEntity<byte[]> downloadStudentCredentials(@PathVariable String key) {
//        byte[] credentialsFile = credentialsCache.get(key);
//
//        if (credentialsFile == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        credentialsCache.remove(key);
//
//        String filename = "Student_Credentials_" +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
//                ".xlsx";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", filename);
//        headers.setContentLength(credentialsFile.length);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(credentialsFile);
//    }
//
//    @GetMapping("/students/template")
//    public ResponseEntity<String> downloadStudentTemplate() {
//        String template = "Full Name,Email,Roll Number,Semester,Stream,Section,Phone,Address\n" +
//                "John Doe,john@example.com,BCA001,1,BCA,A,9876543210,123 Main St\n" +
//                "Jane Smith,jane@example.com,BCA002,1,BCA,A,9876543211,456 Oak Ave";
//        return ResponseEntity.ok(template);
//    }
//
//    // ==================== FACULTY MANAGEMENT ====================
//
//    @PostMapping("/faculty")
//    public ResponseEntity<UserDTO> createFaculty(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createFaculty(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/faculty")
//    public ResponseEntity<List<UserDTO>> getAllFaculty() {
//        List<UserDTO> faculty = adminService.getAllFaculty();
//        return ResponseEntity.ok(faculty);
//    }
//
//    @PostMapping("/faculty/bulk-import")
//    public ResponseEntity<Map<String, Object>> bulkImportFaculty(
//            @RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", false);
//                response.put("message", "File is empty");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            BulkImportResult result = bulkImportService.importFaculty(file);
//
//            String credentialsKey = null;
//            if (result.isHasCredentialsFile()) {
//                credentialsKey = "faculty_credentials_" + System.currentTimeMillis();
//                credentialsCache.put(credentialsKey, result.getCredentialsFile());
//            }
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("totalRecords", result.getTotalRecords());
//            response.put("successCount", result.getSuccessCount());
//            response.put("failureCount", result.getFailureCount());
//            response.put("errors", result.getErrors());
//            response.put("warnings", result.getWarnings());
//            response.put("message", result.getMessage());
//            response.put("hasCredentialsFile", result.isHasCredentialsFile());
//            response.put("credentialsDownloadKey", credentialsKey);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "Failed to import: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @GetMapping("/faculty/credentials/{key}")
//    public ResponseEntity<byte[]> downloadFacultyCredentials(@PathVariable String key) {
//        byte[] credentialsFile = credentialsCache.get(key);
//
//        if (credentialsFile == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        credentialsCache.remove(key);
//
//        String filename = "Faculty_Credentials_" +
//                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
//                ".xlsx";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDispositionFormData("attachment", filename);
//        headers.setContentLength(credentialsFile.length);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .body(credentialsFile);
//    }
//
//    @GetMapping("/faculty/template")
//    public ResponseEntity<String> downloadFacultyTemplate() {
//        String template = "Full Name,Email,Employee ID,Department,Designation,Qualification,Specialization,Phone\n" +
//                "Dr. John Smith,john.smith@college.edu,FAC001,Computer Science,Professor,PhD,AI & ML,9876543210";
//        return ResponseEntity.ok(template);
//    }
//
//    // ==================== SUBJECT MANAGEMENT ====================
//
//    @PostMapping("/subjects/bulk-import")
//    public ResponseEntity<Map<String, Object>> bulkImportSubjects(
//            @RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                Map<String, Object> response = new HashMap<>();
//                response.put("success", false);
//                response.put("message", "File is empty");
//                return ResponseEntity.badRequest().body(response);
//            }
//
//            BulkImportResult result = bulkImportService.importSubjects(file);
//
//            Map<String, Object> response = new HashMap<>();
//            response.put("totalRecords", result.getTotalRecords());
//            response.put("successCount", result.getSuccessCount());
//            response.put("failureCount", result.getFailureCount());
//            response.put("errors", result.getErrors());
//            response.put("warnings", result.getWarnings());
//            response.put("message", result.getMessage());
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "Failed to import: " + e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @GetMapping("/subjects/template")
//    public ResponseEntity<String> downloadSubjectTemplate() {
//        String template = "Subject Code,Subject Name,Semester,Stream,Credits,Description\n" +
//                "BCA101,Data Structures,1,BCA,4,Introduction to data structures and algorithms";
//        return ResponseEntity.ok(template);
//    }
//
//    // ==================== ADMIN MANAGEMENT ====================
//
//    @PostMapping("/admins")
//    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createAdmin(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    @GetMapping("/admins")
//    public ResponseEntity<List<UserDTO>> getAllAdmins() {
//        List<UserDTO> admins = adminService.getAllAdmins();
//        return ResponseEntity.ok(admins);
//    }
//
//    // ==================== USER OPERATIONS ====================
//
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
//        try {
//            UserDTO user = adminService.getUserById(userId);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/users/{userId}")
//    public ResponseEntity<UserDTO> updateUser(
//            @PathVariable Long userId,
//            @Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.updateUser(userId, request);
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/users/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        try {
//            adminService.deleteUser(userId);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/users/{userId}/activate")
//    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
//        try {
//            adminService.activateUser(userId);
//            return ResponseEntity.ok().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}


package com.cia.management_system.controller;

import com.cia.management_system.dto.BulkImportResult;
import com.cia.management_system.dto.ChunkImportRequest;
import com.cia.management_system.dto.CreateUserRequest;
import com.cia.management_system.dto.UserDTO;
import com.cia.management_system.service.AdminService;
import com.cia.management_system.service.BulkImportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final BulkImportService bulkImportService;

    // Store credentials temporarily (in production, use Redis or database)
    private final Map<String, byte[]> credentialsCache = new HashMap<>();

    // Dashboard Stats
    @GetMapping("/stats")
    public ResponseEntity<AdminService.DashboardStats> getDashboardStats() {
        AdminService.DashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // ==================== STUDENT MANAGEMENT ====================

    @PostMapping("/students")
    public ResponseEntity<UserDTO> createStudent(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = adminService.createStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserDTO>> getAllStudents() {
        List<UserDTO> students = adminService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/students/bulk-import")
    public ResponseEntity<Map<String, Object>> bulkImportStudents(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            BulkImportResult result = bulkImportService.importStudents(file);

            String credentialsKey = null;
            if (result.isHasCredentialsFile()) {
                credentialsKey = "student_credentials_" + System.currentTimeMillis();
                credentialsCache.put(credentialsKey, result.getCredentialsFile());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("totalRecords", result.getTotalRecords());
            response.put("successCount", result.getSuccessCount());
            response.put("failureCount", result.getFailureCount());
            response.put("errors", result.getErrors());
            response.put("warnings", result.getWarnings());
            response.put("message", result.getMessage());
            response.put("hasCredentialsFile", result.isHasCredentialsFile());
            response.put("credentialsDownloadKey", credentialsKey);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to import: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/students/credentials/{key}")
    public ResponseEntity<byte[]> downloadStudentCredentials(@PathVariable String key) {
        byte[] credentialsFile = credentialsCache.get(key);

        if (credentialsFile == null) {
            return ResponseEntity.notFound().build();
        }

        credentialsCache.remove(key);

        String filename = "Student_Credentials_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(credentialsFile.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(credentialsFile);
    }

    @GetMapping("/students/template")
    public ResponseEntity<String> downloadStudentTemplate() {
        String template = "Full Name,Email,Roll Number,Semester,Stream,Section,Phone,Address\n" +
                "John Doe,john@example.com,250213000001,1,BCA,A,9876543210,123 Main St\n" +
                "Jane Smith,jane@example.com,250213000002,1,BCA,A,9876543211,456 Oak Ave";
        return ResponseEntity.ok(template);
    }

    // ==================== FACULTY MANAGEMENT ====================

//    @PostMapping("/faculty")
//    public ResponseEntity<UserDTO> createFaculty(@Valid @RequestBody CreateUserRequest request) {
//        try {
//            UserDTO user = adminService.createFaculty(request);
//            return ResponseEntity.status(HttpStatus.CREATED).body(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @PostMapping("/faculty")
    public ResponseEntity<?> createFaculty(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = adminService.createFaculty(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            // ✅ Return the actual error message
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/faculty")
    public ResponseEntity<List<UserDTO>> getAllFaculty() {
        List<UserDTO> faculty = adminService.getAllFaculty();
        return ResponseEntity.ok(faculty);
    }

    @PostMapping("/faculty/bulk-import")
    public ResponseEntity<Map<String, Object>> bulkImportFaculty(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            BulkImportResult result = bulkImportService.importFaculty(file);

            String credentialsKey = null;
            if (result.isHasCredentialsFile()) {
                credentialsKey = "faculty_credentials_" + System.currentTimeMillis();
                credentialsCache.put(credentialsKey, result.getCredentialsFile());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("totalRecords", result.getTotalRecords());
            response.put("successCount", result.getSuccessCount());
            response.put("failureCount", result.getFailureCount());
            response.put("errors", result.getErrors());
            response.put("warnings", result.getWarnings());
            response.put("message", result.getMessage());
            response.put("hasCredentialsFile", result.isHasCredentialsFile());
            response.put("credentialsDownloadKey", credentialsKey);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to import: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/faculty/credentials/{key}")
    public ResponseEntity<byte[]> downloadFacultyCredentials(@PathVariable String key) {
        byte[] credentialsFile = credentialsCache.get(key);

        if (credentialsFile == null) {
            return ResponseEntity.notFound().build();
        }

        credentialsCache.remove(key);

        String filename = "Faculty_Credentials_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(credentialsFile.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(credentialsFile);
    }

    @GetMapping("/faculty/template")
    public ResponseEntity<String> downloadFacultyTemplate() {
        String template = "Full Name,Email,Employee ID,Department,Designation,Qualification,Specialization,Phone\n" +
                "Dr. John Smith,john.smith@college.edu,FAC001,Computer Science,Professor,PhD,AI & ML,9876543210";
        return ResponseEntity.ok(template);
    }

    // ==================== SUBJECT MANAGEMENT ====================

    @PostMapping("/subjects/bulk-import")
    public ResponseEntity<Map<String, Object>> bulkImportSubjects(
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }

            BulkImportResult result = bulkImportService.importSubjects(file);

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

    @GetMapping("/subjects/template")
    public ResponseEntity<String> downloadSubjectTemplate() {
        String template = "Subject Code,Subject Name,Semester,Stream,Credits,Description\n" +
                "BCA101,Data Structures,1,BCA,4,Introduction to data structures and algorithms";
        return ResponseEntity.ok(template);
    }

    // ==================== ADMIN MANAGEMENT ====================

    @PostMapping("/admins")
    public ResponseEntity<UserDTO> createAdmin(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = adminService.createAdmin(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        List<UserDTO> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // ==================== USER OPERATIONS ====================

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        try {
            UserDTO user = adminService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody CreateUserRequest request) {
        try {
            UserDTO user = adminService.updateUser(userId, request);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{userId}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Long userId) {
        try {
            adminService.activateUser(userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }



    // Add this endpoint method
    @PostMapping("/students/bulk-import-chunk")
    public ResponseEntity<Map<String, Object>> bulkImportStudentsChunk(
            @RequestBody ChunkImportRequest request) {
        try {
            BulkImportResult result = bulkImportService.importStudentsChunk(
                    request.getChunk(),
                    request.getChunkNumber()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("successCount", result.getSuccessCount());
            response.put("failureCount", result.getFailureCount());
            response.put("errors", result.getErrors());
            response.put("credentials", result.getCredentials());
            response.put("message", "Chunk " + request.getChunkNumber() + " processed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("Chunk import failed" + e);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to import chunk: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
