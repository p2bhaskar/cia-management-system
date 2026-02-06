package com.cia.management_system.service;

import com.cia.management_system.dto.BulkImportResult;
import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkEnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;

    /**
     * Bulk import enrollments from Excel file
     * Excel Format: Roll Number, Subject Code, Employee ID, Academic Year
     */
    @Transactional
    public BulkImportResult importEnrollments(MultipartFile file) {
        int successCount = 0;
        int failureCount = 0;
        List<String> errors = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            // Start from row 1 (skip header row 0)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    // Column mapping:
                    // A: Roll Number, B: Subject Code, C: Employee ID, D: Academic Year

                    String rollNumber = getCellValue(row.getCell(0));
                    String subjectCode = getCellValue(row.getCell(1));
                    String employeeId = getCellValue(row.getCell(2));
                    String academicYear = getCellValue(row.getCell(3));

                    log.debug("Row {}: rollNumber={}, subjectCode={}, employeeId={}, academicYear={}",
                            i + 1, rollNumber, subjectCode, employeeId, academicYear);

                    // Validate required fields
                    if (isBlank(rollNumber) || isBlank(subjectCode) || isBlank(employeeId)) {
                        errors.add("Row " + (i + 1) + ": Missing required fields (Roll Number, Subject Code, or Employee ID)");
                        failureCount++;
                        continue;
                    }

                    // Set default academic year if not provided
                    if (isBlank(academicYear)) {
                        academicYear = "2024-2025";
                    }

                    rollNumber = rollNumber.trim();
                    subjectCode = subjectCode.trim();
                    employeeId = employeeId.trim();
                    academicYear = academicYear.trim();

                    // Find student by roll number
                    Student student = studentRepository.findByRollNumber(rollNumber)
                            .orElse(null);

                    if (student == null) {
                        errors.add("Row " + (i + 1) + ": Student with roll number '" + rollNumber + "' not found");
                        failureCount++;
                        continue;
                    }

                    // Find subject by subject code
                    Subject subject = subjectRepository.findBySubjectCode(subjectCode)
                            .orElse(null);

                    if (subject == null) {
                        errors.add("Row " + (i + 1) + ": Subject with code '" + subjectCode + "' not found");
                        failureCount++;
                        continue;
                    }

                    // Find faculty by employee ID
                    Faculty faculty = facultyRepository.findByEmployeeId(employeeId)
                            .orElse(null);

                    if (faculty == null) {
                        errors.add("Row " + (i + 1) + ": Faculty with employee ID '" + employeeId + "' not found");
                        failureCount++;
                        continue;
                    }

                    // Check if already enrolled
                    if (enrollmentRepository.findByStudentIdAndSubjectIdAndAcademicYear(
                            student.getId(), subject.getId(), academicYear).isPresent()) {
                        errors.add("Row " + (i + 1) + ": Student '" + rollNumber + "' already enrolled in '" + subjectCode + "' for " + academicYear);
                        failureCount++;
                        continue;
                    }

                    // Create enrollment
                    Enrollment enrollment = new Enrollment();
                    enrollment.setStudent(student);
                    enrollment.setSubject(subject);
                    enrollment.setFaculty(faculty);
                    enrollment.setAcademicYear(academicYear);
                    enrollment.setEnrolledAt(LocalDateTime.now());
                    enrollment.setIsActive(true);

                    enrollmentRepository.save(enrollment);

                    successCount++;
                    log.info("Successfully enrolled: {} in {} with {}", rollNumber, subjectCode, employeeId);

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

        log.info("Bulk enrollment import completed: {} success, {} failed", successCount, failureCount);
        return new BulkImportResult(successCount, failureCount, errors);
    }

    // ==================== HELPER METHODS ====================

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
}