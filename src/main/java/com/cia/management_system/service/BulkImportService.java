//package com.cia.management_system.service;
//
//import com.cia.management_system.dto.BulkImportResult;
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class BulkImportService {
//
//    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//    private final FacultyRepository facultyRepository;
//    private final SubjectRepository subjectRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    // ==================== BULK IMPORT STUDENTS ====================
//
//    @Transactional
//    public BulkImportResult importStudents(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Start from row 1 (skip header row 0)
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Full Name, B: Email, C: Roll Number, D: Semester,
//                    // E: Stream, F: Section, G: Phone, H: Address
//
//                    String fullName = getCellValue(row.getCell(0));
//                    String email = getCellValue(row.getCell(1));
//                    String rollNumber = getCellValue(row.getCell(2));
//                    String semesterStr = getCellValue(row.getCell(3));
//                    String stream = getCellValue(row.getCell(4));
//                    String section = getCellValue(row.getCell(5));
//                    String phone = getCellValue(row.getCell(6));
//                    String address = getCellValue(row.getCell(7));
//
//                    // Validate required fields
//                    if (isBlank(fullName) || isBlank(email) || isBlank(rollNumber) ||
//                            isBlank(semesterStr) || isBlank(stream)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Check if student already exists
//                    if (userRepository.existsByUsername(rollNumber)) {
//                        errors.add("Row " + (i + 1) + ": Roll number " + rollNumber + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    if (userRepository.existsByEmail(email)) {
//                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse semester
//                    int semester;
//                    try {
//                        semester = Integer.parseInt(semesterStr.trim());
//                    } catch (NumberFormatException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid semester value");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Generate password: Student@ + last 4 digits of roll number
//                    String generatedPassword = "Student@" + rollNumber.substring(
//                            Math.max(0, rollNumber.length() - 4)
//                    );
//
//                    // Create User
//                    User user = new User();
//                    user.setUsername(rollNumber.trim());
//                    user.setEmail(email.trim());
//                    user.setPassword(passwordEncoder.encode(generatedPassword));
//                    user.setFullName(fullName.trim());
//                    user.getRoles().add(Role.STUDENT);
//                    user.setEnabled(true);
//                    user.setCreatedAt(java.time.LocalDateTime.now());
//                    User savedUser = userRepository.save(user);
//
//                    // Create Student
//                    Student student = new Student();
//                    student.setUser(savedUser);
//                    student.setRollNumber(rollNumber.trim());
//                    student.setSemester(semester);
//                    student.setStream(Student.Stream.valueOf(stream.trim().toUpperCase()));
//                    student.setSection(section != null ? section.trim() : null);
//                    student.setPhoneNumber(phone);
//                    student.setAddress(address);
//                    student.setAcademicYear("2024-2025");
//                    studentRepository.save(student);
//
//                    successCount++;
//
//                } catch (Exception e) {
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        return new BulkImportResult(successCount, failureCount, errors);
//    }
//
//    // ==================== BULK IMPORT FACULTY ====================
//
//    @Transactional
//    public BulkImportResult importFaculty(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Full Name, B: Email, C: Employee ID, D: Department,
//                    // E: Designation, F: Qualification, G: Specialization, H: Phone
//
//                    String fullName = getCellValue(row.getCell(0));
//                    String email = getCellValue(row.getCell(1));
//                    String employeeId = getCellValue(row.getCell(2));
//                    String department = getCellValue(row.getCell(3));
//                    String designation = getCellValue(row.getCell(4));
//                    String qualification = getCellValue(row.getCell(5));
//                    String specialization = getCellValue(row.getCell(6));
//                    String phone = getCellValue(row.getCell(7));
//
//                    // Validate required fields
//                    if (isBlank(fullName) || isBlank(email) || isBlank(employeeId)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Check if faculty already exists
//                    if (userRepository.existsByUsername(employeeId)) {
//                        errors.add("Row " + (i + 1) + ": Employee ID " + employeeId + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    if (userRepository.existsByEmail(email)) {
//                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Generate password: Faculty@ + last 4 digits of employee ID
//                    String generatedPassword = "Faculty@" + employeeId.substring(
//                            Math.max(0, employeeId.length() - 4)
//                    );
//
//                    // Create User
//                    User user = new User();
//                    user.setUsername(employeeId.trim());
//                    user.setEmail(email.trim());
//                    user.setPassword(passwordEncoder.encode(generatedPassword));
//                    user.setFullName(fullName.trim());
//                    user.getRoles().add(Role.FACULTY);
//                    user.setEnabled(true);
//                    user.setCreatedAt(java.time.LocalDateTime.now());
//                    User savedUser = userRepository.save(user);
//
//                    // Create Faculty
//                    Faculty faculty = new Faculty();
//                    faculty.setUser(savedUser);
//                    faculty.setEmployeeId(employeeId.trim());
//                    faculty.setDepartment(department);
//                    faculty.setDesignation(designation);
//                    faculty.setQualification(qualification);
//                    faculty.setSpecialization(specialization);
//                    faculty.setPhoneNumber(phone);
//                    facultyRepository.save(faculty);
//
//                    successCount++;
//
//                } catch (Exception e) {
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        return new BulkImportResult(successCount, failureCount, errors);
//    }
//
//    // ==================== BULK IMPORT SUBJECTS ====================
//
//    @Transactional
//    public BulkImportResult importSubjects(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Subject Code, B: Subject Name, C: Semester,
//                    // D: Stream, E: Credits, F: Description
//
//                    String subjectCode = getCellValue(row.getCell(0));
//                    String subjectName = getCellValue(row.getCell(1));
//                    String semesterStr = getCellValue(row.getCell(2));
//                    String stream = getCellValue(row.getCell(3));
//                    String creditsStr = getCellValue(row.getCell(4));
//                    String description = getCellValue(row.getCell(5));
//
//                    // Validate required fields
//                    if (isBlank(subjectCode) || isBlank(subjectName) ||
//                            isBlank(semesterStr) || isBlank(stream)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Check if subject already exists
//                    if (subjectRepository.existsBySubjectCode(subjectCode.trim())) {
//                        errors.add("Row " + (i + 1) + ": Subject code " + subjectCode + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse semester
//                    int semester;
//                    try {
//                        semester = Integer.parseInt(semesterStr.trim());
//                    } catch (NumberFormatException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid semester value");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse credits (optional)
//                    Integer credits = null;
//                    if (!isBlank(creditsStr)) {
//                        try {
//                            credits = Integer.parseInt(creditsStr.trim());
//                        } catch (NumberFormatException e) {
//                            // Ignore, keep as null
//                        }
//                    }
//
//                    // Create Subject
//                    Subject subject = new Subject();
//                    subject.setSubjectCode(subjectCode.trim());
//                    subject.setSubjectName(subjectName.trim());
//                    subject.setSemester(semester);
//                    subject.setStream(stream.trim());
//                    subject.setCredits(credits);
//                    subject.setDescription(description);
//                    subject.setAcademicYear("2024-2025");
//                    subject.setIsActive(true);
//                    subjectRepository.save(subject);
//
//                    successCount++;
//
//                } catch (Exception e) {
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        return new BulkImportResult(successCount, failureCount, errors);
//    }
//
//    // ==================== HELPER METHODS ====================
//
//    private String getCellValue(Cell cell) {
//        if (cell == null) return null;
//
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                }
//                return String.valueOf((long) cell.getNumericCellValue());
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                return cell.getCellFormula();
//            default:
//                return null;
//        }
//    }
//
//    private boolean isBlank(String value) {
//        return value == null || value.trim().isEmpty();
//    }
//}
//



//package com.cia.management_system.service;
//
//import com.cia.management_system.dto.BulkImportResult;
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class BulkImportService {
//
//    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//    private final FacultyRepository facultyRepository;
//    private final SubjectRepository subjectRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    // ==================== BULK IMPORT STUDENTS ====================
//
//    @Transactional
//    public BulkImportResult importStudents(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Start from row 1 (skip header row 0)
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Full Name, B: Email, C: Roll Number, D: Semester,
//                    // E: Stream, F: Section, G: Phone, H: Address
//
//                    String fullName = getCellValue(row.getCell(0));
//                    String email = getCellValue(row.getCell(1));
//                    String rollNumber = getCellValue(row.getCell(2));
//                    String semesterStr = getCellValue(row.getCell(3));
//                    String stream = getCellValue(row.getCell(4));
//                    String section = getCellValue(row.getCell(5));
//                    String phone = getCellValue(row.getCell(6));
//                    String address = getCellValue(row.getCell(7));
//
//                    // ✅ ADDED: Log the values for debugging
//                    log.debug("Row {}: fullName={}, email={}, rollNumber={}, semester={}, stream={}, phone={}",
//                            i + 1, fullName, email, rollNumber, semesterStr, stream, phone);
//
//                    // Validate required fields
//                    if (isBlank(fullName) || isBlank(email) || isBlank(rollNumber) ||
//                            isBlank(semesterStr) || isBlank(stream)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ FIXED: Trim and normalize roll number
//                    rollNumber = rollNumber.trim();
//
//                    // Check if student already exists
//                    if (userRepository.existsByUsername(rollNumber)) {
//                        errors.add("Row " + (i + 1) + ": Roll number " + rollNumber + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    if (userRepository.existsByEmail(email.trim())) {
//                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse semester
//                    int semester;
//                    try {
//                        semester = Integer.parseInt(semesterStr.trim());
//                        if (semester < 1 || semester > 8) {
//                            errors.add("Row " + (i + 1) + ": Semester must be between 1 and 8");
//                            failureCount++;
//                            continue;
//                        }
//                    } catch (NumberFormatException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid semester value");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ FIXED: Validate stream enum
//                    Student.Stream streamEnum;
//                    try {
//                        streamEnum = Student.Stream.valueOf(stream.trim().toUpperCase());
//                    } catch (IllegalArgumentException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid stream '" + stream + "'. Must be BCA or MCA");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ FIXED: Generate password safely
//                    String generatedPassword;
//                    if (rollNumber.length() >= 4) {
//                        generatedPassword = "Student@" + rollNumber.substring(rollNumber.length() - 4);
//                    } else {
//                        generatedPassword = "Student@" + rollNumber;
//                    }
//
//                    // Create User
//                    User user = new User();
//                    user.setUsername(rollNumber);
//                    user.setEmail(email.trim());
//                    user.setPassword(passwordEncoder.encode(generatedPassword));
//                    user.setFullName(fullName.trim());
//                    user.getRoles().add(Role.STUDENT);
//                    user.setEnabled(true);
//                    user.setCreatedAt(java.time.LocalDateTime.now());
//
//                    // ✅ ADDED: Set phone in User if provided
//                    if (!isBlank(phone)) {
//                        user.setPhoneNumber(phone.trim());
//                    }
//
//                    User savedUser = userRepository.save(user);
//
//                    // ✅ ADDED: Flush to ensure user is persisted
//                    userRepository.flush();
//
//                    // Create Student
//                    Student student = new Student();
//                    student.setUser(savedUser);
//                    student.setRollNumber(rollNumber);
//                    student.setSemester(semester);
//                    student.setStream(streamEnum);
//                    student.setSection(isBlank(section) ? null : section.trim());
//                    student.setPhoneNumber(isBlank(phone) ? null : phone.trim());
//                    student.setAddress(isBlank(address) ? null : address.trim());
//
//                    // ✅ ADDED: Extract admission year and generate academic year
//                    student.extractAdmissionYear();
//                    if (student.getAdmissionYear() == null) {
//                        // Fallback to current year if extraction fails
//                        student.setAdmissionYear(java.time.LocalDate.now().getYear());
//                    }
//                    student.generateAcademicYear();
//
//                    studentRepository.save(student);
//
//                    successCount++;
//                    log.info("Successfully imported student: {}", rollNumber);
//
//                } catch (Exception e) {
//                    log.error("Error importing row " + (i + 1), e);
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to parse Excel file", e);
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);
//        return new BulkImportResult(successCount, failureCount, errors);
//    }
//
//    // ==================== BULK IMPORT FACULTY ====================
//
//    @Transactional
//    public BulkImportResult importFaculty(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Full Name, B: Email, C: Employee ID, D: Department,
//                    // E: Designation, F: Qualification, G: Specialization, H: Phone
//
//                    String fullName = getCellValue(row.getCell(0));
//                    String email = getCellValue(row.getCell(1));
//                    String employeeId = getCellValue(row.getCell(2));
//                    String department = getCellValue(row.getCell(3));
//                    String designation = getCellValue(row.getCell(4));
//                    String qualification = getCellValue(row.getCell(5));
//                    String specialization = getCellValue(row.getCell(6));
//                    String phone = getCellValue(row.getCell(7));
//
//                    // Validate required fields
//                    if (isBlank(fullName) || isBlank(email) || isBlank(employeeId)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ FIXED: Trim employee ID
//                    employeeId = employeeId.trim();
//
//                    // Check if faculty already exists
//                    if (userRepository.existsByUsername(employeeId)) {
//                        errors.add("Row " + (i + 1) + ": Employee ID " + employeeId + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    if (userRepository.existsByEmail(email.trim())) {
//                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ FIXED: Generate password safely
//                    String generatedPassword;
//                    if (employeeId.length() >= 4) {
//                        generatedPassword = "Faculty@" + employeeId.substring(employeeId.length() - 4);
//                    } else {
//                        generatedPassword = "Faculty@" + employeeId;
//                    }
//
//                    // Create User
//                    User user = new User();
//                    user.setUsername(employeeId);
//                    user.setEmail(email.trim());
//                    user.setPassword(passwordEncoder.encode(generatedPassword));
//                    user.setFullName(fullName.trim());
//                    user.getRoles().add(Role.FACULTY);
//                    user.setEnabled(true);
//                    user.setCreatedAt(java.time.LocalDateTime.now());
//
//                    // ✅ ADDED: Set phone in User if provided
//                    if (!isBlank(phone)) {
//                        user.setPhoneNumber(phone.trim());
//                    }
//
//                    User savedUser = userRepository.save(user);
//
//                    // ✅ ADDED: Flush to ensure user is persisted
//                    userRepository.flush();
//
//                    // Create Faculty
//                    Faculty faculty = new Faculty();
//                    faculty.setUser(savedUser);
//                    faculty.setEmployeeId(employeeId);
//                    faculty.setDepartment(isBlank(department) ? null : department.trim());
//                    faculty.setDesignation(isBlank(designation) ? null : designation.trim());
//                    faculty.setQualification(isBlank(qualification) ? null : qualification.trim());
//                    faculty.setSpecialization(isBlank(specialization) ? null : specialization.trim());
//                    faculty.setPhoneNumber(isBlank(phone) ? null : phone.trim());
//                    facultyRepository.save(faculty);
//
//                    successCount++;
//                    log.info("Successfully imported faculty: {}", employeeId);
//
//                } catch (Exception e) {
//                    log.error("Error importing row " + (i + 1), e);
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to parse Excel file", e);
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);
//        return new BulkImportResult(successCount, failureCount, errors);
//    }
//
//    // ==================== BULK IMPORT SUBJECTS ====================
//
//    @Transactional
//    public BulkImportResult importSubjects(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Subject Code, B: Subject Name, C: Semester,
//                    // D: Stream, E: Credits, F: Description
//
//                    String subjectCode = getCellValue(row.getCell(0));
//                    String subjectName = getCellValue(row.getCell(1));
//                    String semesterStr = getCellValue(row.getCell(2));
//                    String stream = getCellValue(row.getCell(3));
//                    String creditsStr = getCellValue(row.getCell(4));
//                    String description = getCellValue(row.getCell(5));
//
//                    // Validate required fields
//                    if (isBlank(subjectCode) || isBlank(subjectName) ||
//                            isBlank(semesterStr) || isBlank(stream)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ FIXED: Trim subject code
//                    subjectCode = subjectCode.trim();
//
//                    // Check if subject already exists
//                    if (subjectRepository.existsBySubjectCode(subjectCode)) {
//                        errors.add("Row " + (i + 1) + ": Subject code " + subjectCode + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse semester
//                    int semester;
//                    try {
//                        semester = Integer.parseInt(semesterStr.trim());
//                        if (semester < 1 || semester > 8) {
//                            errors.add("Row " + (i + 1) + ": Semester must be between 1 and 8");
//                            failureCount++;
//                            continue;
//                        }
//                    } catch (NumberFormatException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid semester value");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse credits (optional)
//                    Integer credits = null;
//                    if (!isBlank(creditsStr)) {
//                        try {
//                            credits = Integer.parseInt(creditsStr.trim());
//                        } catch (NumberFormatException e) {
//                            // Ignore, keep as null
//                        }
//                    }
//
//                    // Create Subject
//                    Subject subject = new Subject();
//                    subject.setSubjectCode(subjectCode);
//                    subject.setSubjectName(subjectName.trim());
//                    subject.setSemester(semester);
//                    subject.setStream(stream.trim());
//                    subject.setCredits(credits);
//                    subject.setDescription(isBlank(description) ? null : description.trim());
//                    subject.setAcademicYear("2024-2025");
//                    subject.setIsActive(true);
//                    subjectRepository.save(subject);
//
//                    successCount++;
//                    log.info("Successfully imported subject: {}", subjectCode);
//
//                } catch (Exception e) {
//                    log.error("Error importing row " + (i + 1), e);
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to parse Excel file", e);
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);
//        return new BulkImportResult(successCount, failureCount, errors);
//    }
//
//    // ==================== HELPER METHODS ====================
//
//    private String getCellValue(Cell cell) {
//        if (cell == null) return null;
//
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                }
//                // ✅ FIXED: Handle numeric values properly (including phone numbers and roll numbers)
//                double numericValue = cell.getNumericCellValue();
//                // Check if it's a whole number
//                if (numericValue == Math.floor(numericValue)) {
//                    return String.valueOf((long) numericValue);
//                }
//                return String.valueOf(numericValue);
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                // ✅ ADDED: Evaluate formula cells
//                try {
//                    return getCellValue(cell.getCachedFormulaResultType(), cell);
//                } catch (Exception e) {
//                    return cell.getCellFormula();
//                }
//            case BLANK:
//                return null;
//            default:
//                return null;
//        }
//    }
//
//    // ✅ ADDED: Helper method for formula cells
//    private String getCellValue(CellType cellType, Cell cell) {
//        switch (cellType) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                double numericValue = cell.getNumericCellValue();
//                if (numericValue == Math.floor(numericValue)) {
//                    return String.valueOf((long) numericValue);
//                }
//                return String.valueOf(numericValue);
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            default:
//                return null;
//        }
//    }
//
//    private boolean isBlank(String value) {
//        return value == null || value.trim().isEmpty();
//    }
//}


//package com.cia.management_system.service;
//
//import com.cia.management_system.dto.BulkImportResult;
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.ByteArrayOutputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class BulkImportService {
//
//    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//    private final FacultyRepository facultyRepository;
//    private final SubjectRepository subjectRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    // ✅ NEW: Data class to store imported student credentials
//    public static class StudentCredential {
//        public String fullName;
//        public String rollNumber;
//        public String email;
//        public String username;
//        public String password;
//        public String stream;
//        public Integer semester;
//
//        public StudentCredential(String fullName, String rollNumber, String email,
//                                 String username, String password, String stream, Integer semester) {
//            this.fullName = fullName;
//            this.rollNumber = rollNumber;
//            this.email = email;
//            this.username = username;
//            this.password = password;
//            this.stream = stream;
//            this.semester = semester;
//        }
//    }
//
//    // ==================== BULK IMPORT STUDENTS ====================
//
//    @Transactional
//    public BulkImportResult importStudents(MultipartFile file) {
//        int successCount = 0;
//        int failureCount = 0;
//        List<String> errors = new ArrayList<>();
//        List<StudentCredential> credentials = new ArrayList<>(); // ✅ Store credentials
//
//        try (InputStream is = file.getInputStream()) {
//            Workbook workbook = WorkbookFactory.create(is);
//            Sheet sheet = workbook.getSheetAt(0);
//
//            // Start from row 1 (skip header row 0)
//            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//                Row row = sheet.getRow(i);
//                if (row == null) continue;
//
//                try {
//                    // Column mapping:
//                    // A: Full Name, B: Email, C: Roll Number, D: Semester,
//                    // E: Stream, F: Section, G: Phone, H: Address
//
//                    String fullName = getCellValue(row.getCell(0));
//                    String email = getCellValue(row.getCell(1));
//                    String rollNumber = getCellValue(row.getCell(2));
//                    String semesterStr = getCellValue(row.getCell(3));
//                    String stream = getCellValue(row.getCell(4));
//                    String section = getCellValue(row.getCell(5));
//                    String phone = getCellValue(row.getCell(6));
//                    String address = getCellValue(row.getCell(7));
//
//                    log.debug("Row {}: fullName={}, email={}, rollNumber={}, semester={}, stream={}, phone={}",
//                            i + 1, fullName, email, rollNumber, semesterStr, stream, phone);
//
//                    // Validate required fields
//                    if (isBlank(fullName) || isBlank(email) || isBlank(rollNumber) ||
//                            isBlank(semesterStr) || isBlank(stream)) {
//                        errors.add("Row " + (i + 1) + ": Missing required fields");
//                        failureCount++;
//                        continue;
//                    }
//
//                    rollNumber = rollNumber.trim();
//
//                    // Check if student already exists
//                    if (userRepository.existsByUsername(rollNumber)) {
//                        errors.add("Row " + (i + 1) + ": Roll number " + rollNumber + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    if (userRepository.existsByEmail(email.trim())) {
//                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Parse semester
//                    int semester;
//                    try {
//                        semester = Integer.parseInt(semesterStr.trim());
//                        if (semester < 1 || semester > 8) {
//                            errors.add("Row " + (i + 1) + ": Semester must be between 1 and 8");
//                            failureCount++;
//                            continue;
//                        }
//                    } catch (NumberFormatException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid semester value");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // Validate stream enum
//                    Student.Stream streamEnum;
//                    try {
//                        streamEnum = Student.Stream.valueOf(stream.trim().toUpperCase());
//                    } catch (IllegalArgumentException e) {
//                        errors.add("Row " + (i + 1) + ": Invalid stream '" + stream + "'. Must be BCA or MCA");
//                        failureCount++;
//                        continue;
//                    }
//
//                    // ✅ Generate password
//                    String generatedPassword;
//                    if (rollNumber.length() >= 4) {
//                        generatedPassword = "Student@" + rollNumber.substring(rollNumber.length() - 4);
//                    } else {
//                        generatedPassword = "Student@" + rollNumber;
//                    }
//
//                    // Create User
//                    User user = new User();
//                    user.setUsername(rollNumber);
//                    user.setEmail(email.trim());
//                    user.setPassword(passwordEncoder.encode(generatedPassword));
//                    user.setFullName(fullName.trim());
//                    user.getRoles().add(Role.STUDENT);
//                    user.setEnabled(true);
//                    user.setCreatedAt(java.time.LocalDateTime.now());
//
//                    if (!isBlank(phone)) {
//                        user.setPhoneNumber(phone.trim());
//                    }
//
//                    User savedUser = userRepository.save(user);
//                    userRepository.flush();
//
//                    // Create Student
//                    Student student = new Student();
//                    student.setUser(savedUser);
//                    student.setRollNumber(rollNumber);
//                    student.setSemester(semester);
//                    student.setStream(streamEnum);
//                    student.setSection(isBlank(section) ? null : section.trim());
//                    student.setPhoneNumber(isBlank(phone) ? null : phone.trim());
//                    student.setAddress(isBlank(address) ? null : address.trim());
//
//                    student.extractAdmissionYear();
//                    if (student.getAdmissionYear() == null) {
//                        student.setAdmissionYear(java.time.LocalDate.now().getYear());
//                    }
//                    student.generateAcademicYear();
//
//                    studentRepository.save(student);
//
//                    // ✅ Store credential for Excel export
//                    credentials.add(new StudentCredential(
//                            fullName.trim(),
//                            rollNumber,
//                            email.trim(),
//                            rollNumber,  // username = rollNumber
//                            generatedPassword,
//                            stream.trim().toUpperCase(),
//                            semester
//                    ));
//
//                    successCount++;
//                    log.info("Successfully imported student: {}", rollNumber);
//
//                } catch (Exception e) {
//                    log.error("Error importing row " + (i + 1), e);
//                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
//                    failureCount++;
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("Failed to parse Excel file", e);
//            errors.add("Failed to parse Excel file: " + e.getMessage());
//            return new BulkImportResult(0, 0, errors);
//        }
//
//        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);
//
//        BulkImportResult result = new BulkImportResult(successCount, failureCount, errors);
//
//        // ✅ Generate credentials Excel if there are successful imports
//        if (successCount > 0) {
//            try {
//                byte[] excelBytes = generateCredentialsExcel(credentials);
//                result.setCredentialsFile(excelBytes);
//            } catch (Exception e) {
//                log.error("Failed to generate credentials Excel", e);
//                result.addWarning("Successfully imported students but failed to generate credentials file");
//            }
//        }
//
//        return result;
//    }
//
//    // ✅ NEW: Generate Excel file with credentials
//    private byte[] generateCredentialsExcel(List<StudentCredential> credentials) throws Exception {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Student Credentials");
//
//        // Create header style
//        CellStyle headerStyle = workbook.createCellStyle();
//        Font headerFont = workbook.createFont();
//        headerFont.setBold(true);
//        headerFont.setFontHeightInPoints((short) 12);
//        headerFont.setColor(IndexedColors.WHITE.getIndex());
//        headerStyle.setFont(headerFont);
//        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
//        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        headerStyle.setAlignment(HorizontalAlignment.CENTER);
//        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        // Create data style
//        CellStyle dataStyle = workbook.createCellStyle();
//        dataStyle.setAlignment(HorizontalAlignment.LEFT);
//        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//
//        // Create header row
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"S.No.", "Full Name", "Roll Number", "Email", "Username", "Password", "Stream", "Semester"};
//
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//            cell.setCellStyle(headerStyle);
//        }
//
//        // Add data rows
//        int rowNum = 1;
//        for (StudentCredential cred : credentials) {
//            Row row = sheet.createRow(rowNum);
//
//            row.createCell(0).setCellValue(rowNum);
//            row.createCell(1).setCellValue(cred.fullName);
//            row.createCell(2).setCellValue(cred.rollNumber);
//            row.createCell(3).setCellValue(cred.email);
//            row.createCell(4).setCellValue(cred.username);
//            row.createCell(5).setCellValue(cred.password);
//            row.createCell(6).setCellValue(cred.stream);
//            row.createCell(7).setCellValue(cred.semester);
//
//            // Apply data style to all cells in row
//            for (int i = 0; i < headers.length; i++) {
//                row.getCell(i).setCellStyle(dataStyle);
//            }
//
//            rowNum++;
//        }
//
//        // Auto-size columns
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//            // Add some extra width for padding
//            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
//        }
//
//        // Add title
//        Row titleRow = sheet.createRow(rowNum + 1);
//        Cell titleCell = titleRow.createCell(0);
//        titleCell.setCellValue("⚠️ IMPORTANT: Keep this file secure. Share credentials individually with students.");
//
//        CellStyle warningStyle = workbook.createCellStyle();
//        Font warningFont = workbook.createFont();
//        warningFont.setBold(true);
//        warningFont.setColor(IndexedColors.RED.getIndex());
//        warningStyle.setFont(warningFont);
//        titleCell.setCellStyle(warningStyle);
//
//        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowNum + 1, rowNum + 1, 0, 7));
//
//        // Convert to byte array
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//
//        return outputStream.toByteArray();
//    }
//
//    // ==================== BULK IMPORT FACULTY ====================
//    // ... (keep the existing faculty import method)
//
//    // ==================== BULK IMPORT SUBJECTS ====================
//    // ... (keep the existing subject import method)
//
//    // ==================== HELPER METHODS ====================
//
//    private String getCellValue(Cell cell) {
//        if (cell == null) return null;
//
//        switch (cell.getCellType()) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    return cell.getDateCellValue().toString();
//                }
//                double numericValue = cell.getNumericCellValue();
//                if (numericValue == Math.floor(numericValue)) {
//                    return String.valueOf((long) numericValue);
//                }
//                return String.valueOf(numericValue);
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            case FORMULA:
//                try {
//                    return getCellValue(cell.getCachedFormulaResultType(), cell);
//                } catch (Exception e) {
//                    return cell.getCellFormula();
//                }
//            case BLANK:
//                return null;
//            default:
//                return null;
//        }
//    }
//
//    private String getCellValue(CellType cellType, Cell cell) {
//        switch (cellType) {
//            case STRING:
//                return cell.getStringCellValue();
//            case NUMERIC:
//                double numericValue = cell.getNumericCellValue();
//                if (numericValue == Math.floor(numericValue)) {
//                    return String.valueOf((long) numericValue);
//                }
//                return String.valueOf(numericValue);
//            case BOOLEAN:
//                return String.valueOf(cell.getBooleanCellValue());
//            default:
//                return null;
//        }
//    }
//
//    private boolean isBlank(String value) {
//        return value == null || value.trim().isEmpty();
//    }
//}


//bulk import option added

package com.cia.management_system.service;

import com.cia.management_system.dto.BulkImportResult;
import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkImportService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;

    // ==================== DATA CLASSES ====================

    public static class StudentCredential {
        public String fullName;
        public String rollNumber;
        public String email;
        public String username;
        public String password;
        public String stream;
        public Integer semester;

        public StudentCredential(String fullName, String rollNumber, String email,
                                 String username, String password, String stream, Integer semester) {
            this.fullName = fullName;
            this.rollNumber = rollNumber;
            this.email = email;
            this.username = username;
            this.password = password;
            this.stream = stream;
            this.semester = semester;
        }
    }

    public static class FacultyCredential {
        public String fullName;
        public String employeeId;
        public String email;
        public String username;
        public String password;
        public String department;
        public String designation;

        public FacultyCredential(String fullName, String employeeId, String email,
                                 String username, String password, String department, String designation) {
            this.fullName = fullName;
            this.employeeId = employeeId;
            this.email = email;
            this.username = username;
            this.password = password;
            this.department = department;
            this.designation = designation;
        }
    }

    // ==================== BULK IMPORT STUDENTS ====================

    @Transactional
    public BulkImportResult importStudents(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        List<StudentCredential> credentials = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String fullName = getCellValue(row.getCell(0));
                    String email = getCellValue(row.getCell(1));
                    String rollNumber = getCellValue(row.getCell(2));
                    String semesterStr = getCellValue(row.getCell(3));
                    String stream = getCellValue(row.getCell(4));
                    String section = getCellValue(row.getCell(5));
                    String phone = getCellValue(row.getCell(6));
                    String address = getCellValue(row.getCell(7));

                    if (isBlank(fullName) || isBlank(email) || isBlank(rollNumber) ||
                            isBlank(semesterStr) || isBlank(stream)) {
                        errors.add("Row " + (i + 1) + ": Missing required fields");
                        failureCount++;
                        continue;
                    }

                    rollNumber = rollNumber.trim();

                    if (userRepository.existsByUsername(rollNumber)) {
                        errors.add("Row " + (i + 1) + ": Roll number " + rollNumber + " already exists");
                        failureCount++;
                        continue;
                    }

                    if (userRepository.existsByEmail(email.trim())) {
                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
                        failureCount++;
                        continue;
                    }

                    int semester;
                    try {
                        semester = Integer.parseInt(semesterStr.trim());
                        if (semester < 1 || semester > 8) {
                            errors.add("Row " + (i + 1) + ": Semester must be between 1 and 8");
                            failureCount++;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        errors.add("Row " + (i + 1) + ": Invalid semester value");
                        failureCount++;
                        continue;
                    }

                    Student.Stream streamEnum;
                    try {
                        streamEnum = Student.Stream.valueOf(stream.trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        errors.add("Row " + (i + 1) + ": Invalid stream '" + stream + "'. Must be BCA or MCA");
                        failureCount++;
                        continue;
                    }

                    String generatedPassword;
                    if (rollNumber.length() >= 4) {
                        generatedPassword = "Student@" + rollNumber.substring(rollNumber.length() - 4);
                    } else {
                        generatedPassword = "Student@" + rollNumber;
                    }

                    User user = new User();
                    user.setUsername(rollNumber);
                    user.setEmail(email.trim());
                    user.setPassword(passwordEncoder.encode(generatedPassword));
                    user.setFullName(fullName.trim());
                    user.getRoles().add(Role.STUDENT);
                    user.setEnabled(true);
                    user.setCreatedAt(java.time.LocalDateTime.now());

                    if (!isBlank(phone)) {
                        user.setPhoneNumber(phone.trim());
                    }

                    User savedUser = userRepository.save(user);
                    userRepository.flush();

                    Student student = new Student();
                    student.setUser(savedUser);
                    student.setRollNumber(rollNumber);
                    student.setSemester(semester);
                    student.setStream(streamEnum);
                    student.setSection(isBlank(section) ? null : section.trim());
                    student.setPhoneNumber(isBlank(phone) ? null : phone.trim());
                    student.setAddress(isBlank(address) ? null : address.trim());

                    student.extractAdmissionYear();
                    if (student.getAdmissionYear() == null) {
                        student.setAdmissionYear(java.time.LocalDate.now().getYear());
                    }
                    student.generateAcademicYear();

                    studentRepository.save(student);

                    credentials.add(new StudentCredential(
                            fullName.trim(),
                            rollNumber,
                            email.trim(),
                            rollNumber,
                            generatedPassword,
                            stream.trim().toUpperCase(),
                            semester
                    ));

                    successCount++;
                    log.info("Successfully imported student: {}", rollNumber);

                } catch (Exception e) {
                    log.error("Error importing row " + (i + 1), e);
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    failureCount++;
                }
            }

        } catch (Exception e) {
            log.error("Failed to parse Excel file", e);
            errors.add("Failed to parse Excel file: " + e.getMessage());
            return new BulkImportResult(0, 0, errors);
        }

        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);

        BulkImportResult result = new BulkImportResult(successCount, failureCount, errors);

        if (successCount > 0) {
            try {
                byte[] excelBytes = generateStudentCredentialsExcel(credentials);
                result.setCredentialsFile(excelBytes);
            } catch (Exception e) {
                log.error("Failed to generate credentials Excel", e);
                result.addWarning("Successfully imported students but failed to generate credentials file");
            }
        }

        return result;
    }

    // ==================== BULK IMPORT FACULTY ====================

    @Transactional
    public BulkImportResult importFaculty(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        List<FacultyCredential> credentials = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String fullName = getCellValue(row.getCell(0));
                    String email = getCellValue(row.getCell(1));
                    String employeeId = getCellValue(row.getCell(2));
                    String department = getCellValue(row.getCell(3));
                    String designation = getCellValue(row.getCell(4));
                    String qualification = getCellValue(row.getCell(5));
                    String specialization = getCellValue(row.getCell(6));
                    String phone = getCellValue(row.getCell(7));

                    if (isBlank(fullName) || isBlank(email) || isBlank(employeeId)) {
                        errors.add("Row " + (i + 1) + ": Missing required fields");
                        failureCount++;
                        continue;
                    }

                    employeeId = employeeId.trim();

                    if (userRepository.existsByUsername(employeeId)) {
                        errors.add("Row " + (i + 1) + ": Employee ID " + employeeId + " already exists");
                        failureCount++;
                        continue;
                    }

                    if (userRepository.existsByEmail(email.trim())) {
                        errors.add("Row " + (i + 1) + ": Email " + email + " already exists");
                        failureCount++;
                        continue;
                    }

                    String generatedPassword;
                    if (employeeId.length() >= 4) {
                        generatedPassword = "Faculty@" + employeeId.substring(employeeId.length() - 4);
                    } else {
                        generatedPassword = "Faculty@" + employeeId;
                    }

                    User user = new User();
                    user.setUsername(employeeId);
                    user.setEmail(email.trim());
                    user.setPassword(passwordEncoder.encode(generatedPassword));
                    user.setFullName(fullName.trim());
                    user.getRoles().add(Role.FACULTY);
                    user.setEnabled(true);
                    user.setCreatedAt(java.time.LocalDateTime.now());

                    if (!isBlank(phone)) {
                        user.setPhoneNumber(phone.trim());
                    }

                    User savedUser = userRepository.save(user);
                    userRepository.flush();

                    Faculty faculty = new Faculty();
                    faculty.setUser(savedUser);
                    faculty.setEmployeeId(employeeId);
                    faculty.setDepartment(isBlank(department) ? null : department.trim());
                    faculty.setDesignation(isBlank(designation) ? null : designation.trim());
                    faculty.setQualification(isBlank(qualification) ? null : qualification.trim());
                    faculty.setSpecialization(isBlank(specialization) ? null : specialization.trim());
                    faculty.setPhoneNumber(isBlank(phone) ? null : phone.trim());
                    facultyRepository.save(faculty);

                    credentials.add(new FacultyCredential(
                            fullName.trim(),
                            employeeId,
                            email.trim(),
                            employeeId,
                            generatedPassword,
                            isBlank(department) ? "N/A" : department.trim(),
                            isBlank(designation) ? "N/A" : designation.trim()
                    ));

                    successCount++;
                    log.info("Successfully imported faculty: {}", employeeId);

                } catch (Exception e) {
                    log.error("Error importing row " + (i + 1), e);
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    failureCount++;
                }
            }

        } catch (Exception e) {
            log.error("Failed to parse Excel file", e);
            errors.add("Failed to parse Excel file: " + e.getMessage());
            return new BulkImportResult(0, 0, errors);
        }

        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);

        BulkImportResult result = new BulkImportResult(successCount, failureCount, errors);

        if (successCount > 0) {
            try {
                byte[] excelBytes = generateFacultyCredentialsExcel(credentials);
                result.setCredentialsFile(excelBytes);
            } catch (Exception e) {
                log.error("Failed to generate credentials Excel", e);
                result.addWarning("Successfully imported faculty but failed to generate credentials file");
            }
        }

        return result;
    }

    // ==================== BULK IMPORT SUBJECTS ====================

    @Transactional
    public BulkImportResult importSubjects(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String subjectCode = getCellValue(row.getCell(0));
                    String subjectName = getCellValue(row.getCell(1));
                    String semesterStr = getCellValue(row.getCell(2));
                    String stream = getCellValue(row.getCell(3));
                    String creditsStr = getCellValue(row.getCell(4));
                    String description = getCellValue(row.getCell(5));

                    if (isBlank(subjectCode) || isBlank(subjectName) ||
                            isBlank(semesterStr) || isBlank(stream)) {
                        errors.add("Row " + (i + 1) + ": Missing required fields");
                        failureCount++;
                        continue;
                    }

                    subjectCode = subjectCode.trim();

                    if (subjectRepository.existsBySubjectCode(subjectCode)) {
                        errors.add("Row " + (i + 1) + ": Subject code " + subjectCode + " already exists");
                        failureCount++;
                        continue;
                    }

                    int semester;
                    try {
                        semester = Integer.parseInt(semesterStr.trim());
                        if (semester < 1 || semester > 8) {
                            errors.add("Row " + (i + 1) + ": Semester must be between 1 and 8");
                            failureCount++;
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        errors.add("Row " + (i + 1) + ": Invalid semester value");
                        failureCount++;
                        continue;
                    }

                    Integer credits = null;
                    if (!isBlank(creditsStr)) {
                        try {
                            credits = Integer.parseInt(creditsStr.trim());
                        } catch (NumberFormatException e) {
                            // Ignore, keep as null
                        }
                    }

                    Subject subject = new Subject();
                    subject.setSubjectCode(subjectCode);
                    subject.setSubjectName(subjectName.trim());
                    subject.setSemester(semester);
                    subject.setStream(stream.trim());
                    subject.setCredits(credits);
                    subject.setDescription(isBlank(description) ? null : description.trim());
                    subject.setAcademicYear("2024-2025");
                    subject.setIsActive(true);
                    subjectRepository.save(subject);

                    successCount++;
                    log.info("Successfully imported subject: {}", subjectCode);

                } catch (Exception e) {
                    log.error("Error importing row " + (i + 1), e);
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    failureCount++;
                }
            }

        } catch (Exception e) {
            log.error("Failed to parse Excel file", e);
            errors.add("Failed to parse Excel file: " + e.getMessage());
            return new BulkImportResult(0, 0, errors);
        }

        log.info("Bulk import completed: {} success, {} failed", successCount, failureCount);
        return new BulkImportResult(successCount, failureCount, errors);
    }

    // ==================== CREDENTIAL EXCEL GENERATORS ====================

    private byte[] generateStudentCredentialsExcel(List<StudentCredential> credentials) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Student Credentials");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"S.No.", "Full Name", "Roll Number", "Email", "Username", "Password", "Stream", "Semester"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (StudentCredential cred : credentials) {
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(cred.fullName);
            row.createCell(2).setCellValue(cred.rollNumber);
            row.createCell(3).setCellValue(cred.email);
            row.createCell(4).setCellValue(cred.username);
            row.createCell(5).setCellValue(cred.password);
            row.createCell(6).setCellValue(cred.stream);
            row.createCell(7).setCellValue(cred.semester);

            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }

            rowNum++;
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }

        addSecurityWarning(sheet, rowNum, headers.length, workbook);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    private byte[] generateFacultyCredentialsExcel(List<FacultyCredential> credentials) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Faculty Credentials");

        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"S.No.", "Full Name", "Employee ID", "Email", "Username", "Password", "Department", "Designation"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (FacultyCredential cred : credentials) {
            Row row = sheet.createRow(rowNum);

            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(cred.fullName);
            row.createCell(2).setCellValue(cred.employeeId);
            row.createCell(3).setCellValue(cred.email);
            row.createCell(4).setCellValue(cred.username);
            row.createCell(5).setCellValue(cred.password);
            row.createCell(6).setCellValue(cred.department);
            row.createCell(7).setCellValue(cred.designation);

            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }

            rowNum++;
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
        }

        addSecurityWarning(sheet, rowNum, headers.length, workbook);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    // ==================== HELPER METHODS ====================

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return headerStyle;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.LEFT);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return dataStyle;
    }

    private void addSecurityWarning(Sheet sheet, int rowNum, int colCount, Workbook workbook) {
        Row titleRow = sheet.createRow(rowNum + 1);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("⚠️ IMPORTANT: Keep this file secure. Share credentials individually with users.");

        CellStyle warningStyle = workbook.createCellStyle();
        Font warningFont = workbook.createFont();
        warningFont.setBold(true);
        warningFont.setColor(IndexedColors.RED.getIndex());
        warningStyle.setFont(warningFont);
        titleCell.setCellStyle(warningStyle);

        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowNum + 1, rowNum + 1, 0, colCount - 1));
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((long) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return getCellValue(cell.getCachedFormulaResultType(), cell);
                } catch (Exception e) {
                    return cell.getCellFormula();
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    private String getCellValue(CellType cellType, Cell cell) {
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((long) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }



    // Add this method to your BulkImportService class
    @Transactional
    public BulkImportResult importStudentsChunk(List<Map<String, Object>> chunk, int chunkNumber) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();
        List<Map<String, String>> credentials = new ArrayList<>();

        log.info("Processing student chunk {} with {} rows", chunkNumber, chunk.size());

        for (int i = 0; i < chunk.size(); i++) {
            Map<String, Object> row = chunk.get(i);
            int rowNum = ((chunkNumber - 1) * chunk.size()) + i + 1;

            try {
                String fullName = getStringValue(row.get("Full Name"));
                String email = getStringValue(row.get("Email"));
                String rollNumber = getStringValue(row.get("Roll Number"));
                String semesterStr = getStringValue(row.get("Semester"));
                String stream = getStringValue(row.get("Stream"));
                String section = getStringValue(row.get("Section"));
                String phone = getStringValue(row.get("Phone"));
                String address = getStringValue(row.get("Address"));

                // Validation
                if (isBlank(fullName) || isBlank(email) || isBlank(rollNumber) ||
                        isBlank(semesterStr) || isBlank(stream)) {
                    errors.add("Row " + rowNum + ": Missing required fields");
                    failureCount++;
                    continue;
                }

                rollNumber = rollNumber.trim();

                // Check duplicates
                if (userRepository.existsByUsername(rollNumber)) {
                    errors.add("Row " + rowNum + ": Roll number already exists");
                    failureCount++;
                    continue;
                }

                if (userRepository.existsByEmail(email.trim())) {
                    errors.add("Row " + rowNum + ": Email already exists");
                    failureCount++;
                    continue;
                }

                int semester = Integer.parseInt(semesterStr.trim());
                if (semester < 1 || semester > 8) {
                    errors.add("Row " + rowNum + ": Semester must be 1-8");
                    failureCount++;
                    continue;
                }

                Student.Stream streamEnum;
                try {
                    streamEnum = Student.Stream.valueOf(stream.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    errors.add("Row " + rowNum + ": Invalid stream. Must be BCA or MCA");
                    failureCount++;
                    continue;
                }

                String generatedPassword = generateStudentPassword(rollNumber);

                // Create user
                User user = new User();
                user.setUsername(rollNumber);
                user.setEmail(email.trim());
                user.setPassword(passwordEncoder.encode(generatedPassword));
                user.setFullName(fullName.trim());
                user.getRoles().add(Role.STUDENT);
                user.setEnabled(true);
                user.setCreatedAt(java.time.LocalDateTime.now());

                if (!isBlank(phone)) {
                    user.setPhoneNumber(phone.trim());
                }

                User savedUser = userRepository.save(user);
                userRepository.flush();

                // Create student
                Student student = new Student();
                student.setUser(savedUser);
                student.setRollNumber(rollNumber);
                student.setSemester(semester);
                student.setStream(streamEnum);
                student.setSection(isBlank(section) ? null : section.trim());
                student.setPhoneNumber(isBlank(phone) ? null : phone.trim());
                student.setAddress(isBlank(address) ? null : address.trim());
                student.setAdmissionYear(java.time.LocalDate.now().getYear());
                student.generateAcademicYear();

                studentRepository.save(student);

                // Add credentials
                Map<String, String> credential = new HashMap<>();
                credential.put("fullName", fullName.trim());
                credential.put("rollNumber", rollNumber);
                credential.put("email", email.trim());
                credential.put("username", rollNumber);
                credential.put("password", generatedPassword);
                credential.put("stream", stream.trim().toUpperCase());
                credential.put("semester", String.valueOf(semester));
                credentials.add(credential);

                successCount++;
                log.info("Successfully imported student: {}", rollNumber);

            } catch (Exception e) {
                log.error("Error in row {}: {}", rowNum, e.getMessage());
                errors.add("Row " + rowNum + ": " + e.getMessage());
                failureCount++;
            }
        }

        BulkImportResult result = new BulkImportResult(successCount, failureCount, errors);
        result.setCredentials(credentials);
        return result;
    }

    private String generateStudentPassword(String rollNumber) {
        if (rollNumber.length() >= 4) {
            return "Student@" + rollNumber.substring(rollNumber.length() - 4);
        }
        return "Student@" + rollNumber;
    }

    private String getStringValue(Object value) {
        if (value == null) return null;
        return value.toString();
    }
}