package com.cia.management_system.service;



import com.cia.management_system.model.AttendanceSummary;
import com.cia.management_system.model.Faculty;
import com.cia.management_system.model.Student;
import com.cia.management_system.model.Subject;
import com.cia.management_system.repository.AttendanceSummaryRepository;
import com.cia.management_system.repository.FacultyRepository;
import com.cia.management_system.repository.StudentRepository;
import com.cia.management_system.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelParserService {

    private final AttendanceSummaryRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;

    @Transactional
    public AttendanceParseResult parseAttendanceExcel(
            MultipartFile file,
            Long subjectId,
            String username) throws IOException {

        log.info("Starting attendance Excel parse for subject ID: {}", subjectId);

        Faculty faculty = facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        AttendanceParseResult result = new AttendanceParseResult();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int processedCount = 0;
            int errorCount = 0;
            List<String> errors = new ArrayList<>();

            // Start from row 3 (index 3 = Row 4 in Excel, since 0-indexed)
            for (int rowIndex = 3; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);

                if (row == null) {
                    continue;
                }

                try {
                    // Column B (index 1): Roll Number
                    Cell rollNumberCell = row.getCell(1);
                    if (rollNumberCell == null || getCellValueAsString(rollNumberCell).trim().isEmpty()) {
                        continue; // Skip empty rows
                    }

                    String rollNumber = getCellValueAsString(rollNumberCell).trim();

                    // Column Q (index 16): Attendance Percentage (decimal format)
                    Cell percentageCell = row.getCell(16);
                    if (percentageCell == null) {
                        errors.add("Row " + (rowIndex + 1) + ": Missing attendance percentage for roll " + rollNumber);
                        errorCount++;
                        continue;
                    }

                    double attendancePercentage = getNumericCellValue(percentageCell);

                    // Optional: Column N (index 13): Classes Attended
                    Cell attendedCell = row.getCell(13);
                    Integer classesAttended = attendedCell != null ?
                            (int) getNumericCellValue(attendedCell) : null;

                    // Optional: Column P (index 15): Total Classes
                    Cell totalCell = row.getCell(15);
                    Integer totalClasses = totalCell != null ?
                            (int) getNumericCellValue(totalCell) : null;

                    // Calculate marks: percentage × 7
                    double marksObtained = attendancePercentage * 7.0;

                    // Find student by roll number
                    Student student = studentRepository.findByRollNumber(rollNumber)
                            .orElse(null);

                    if (student == null) {
                        errors.add("Row " + (rowIndex + 1) + ": Student not found with roll number: " + rollNumber);
                        errorCount++;
                        continue;
                    }

                    // Update or create attendance record
                    AttendanceSummary attendance = attendanceRepository
                            .findByStudentIdAndSubjectId(student.getId(), subjectId)
                            .orElse(new AttendanceSummary());

                    attendance.setStudent(student);
                    attendance.setSubject(subject);
                    attendance.setAttendancePercentage(attendancePercentage);
                    attendance.setMarksObtained(marksObtained);
                    attendance.setClassesAttended(classesAttended);
                    attendance.setTotalClasses(totalClasses);
                    attendance.setLastUpdated(LocalDateTime.now());
                    attendance.setUpdatedBy(faculty);

                    attendanceRepository.save(attendance);
                    processedCount++;

                    log.debug("Processed: Roll={}, Percentage={}, Marks={}",
                            rollNumber, attendancePercentage, marksObtained);

                } catch (Exception e) {
                    errors.add("Row " + (rowIndex + 1) + ": " + e.getMessage());
                    errorCount++;
                    log.error("Error processing row {}: {}", rowIndex + 1, e.getMessage());
                }
            }

            result.setSuccess(true);
            result.setProcessedRecords(processedCount);
            result.setErrorCount(errorCount);
            result.setErrors(errors);
            result.setMessage(String.format("Processed %d records successfully. %d errors.",
                    processedCount, errorCount));

            log.info("Attendance parse complete: {} processed, {} errors", processedCount, errorCount);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Failed to parse Excel file: " + e.getMessage());
            log.error("Excel parsing failed", e);
            throw new RuntimeException("Failed to parse Excel file", e);
        }

        return result;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if it's a date
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                // Convert number to string (remove decimal if it's a whole number)
                double numValue = cell.getNumericCellValue();
                if (numValue == (long) numValue) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private double getNumericCellValue(Cell cell) {
        if (cell == null) {
            return 0.0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            case FORMULA:
                return cell.getNumericCellValue();
            default:
                return 0.0;
        }
    }

    @lombok.Data
    public static class AttendanceParseResult {
        private boolean success;
        private String message;
        private int processedRecords;
        private int errorCount;
        private List<String> errors = new ArrayList<>();
    }
}