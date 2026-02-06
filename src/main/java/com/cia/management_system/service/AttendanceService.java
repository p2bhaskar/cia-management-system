package com.cia.management_system.service;



import com.cia.management_system.dto.AttendanceSummaryDTO;
import com.cia.management_system.model.AttendanceSummary;
import com.cia.management_system.repository.AttendanceSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceSummaryRepository attendanceRepository;
    private final ExcelParserService excelParserService;

    @Transactional
    public ExcelParserService.AttendanceParseResult uploadAttendanceExcel(
            MultipartFile file,
            Long subjectId,
            String username) throws IOException {

        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            throw new RuntimeException("Invalid file format. Please upload Excel file (.xlsx or .xls)");
        }

        return excelParserService.parseAttendanceExcel(file, subjectId, username);
    }

    @Transactional(readOnly = true)
    public List<AttendanceSummaryDTO> getAttendanceBySubject(Long subjectId) {
        List<AttendanceSummary> attendanceList = attendanceRepository.findBySubjectId(subjectId);

        return attendanceList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceSummaryDTO> getAttendanceByStudent(Long studentId) {
        List<AttendanceSummary> attendanceList = attendanceRepository.findByStudentId(studentId);

        return attendanceList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AttendanceSummaryDTO getAttendance(Long studentId, Long subjectId) {
        return attendanceRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private AttendanceSummaryDTO convertToDTO(AttendanceSummary attendance) {
        AttendanceSummaryDTO dto = new AttendanceSummaryDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudent().getId());
        dto.setStudentName(attendance.getStudent().getUser().getFullName());
        dto.setRollNumber(attendance.getStudent().getRollNumber());
        dto.setSubjectId(attendance.getSubject().getId());
        dto.setSubjectName(attendance.getSubject().getSubjectName());
        dto.setAttendancePercentage(attendance.getAttendancePercentage());
        dto.setMarksObtained(attendance.getMarksObtained());
        dto.setClassesAttended(attendance.getClassesAttended());
        dto.setTotalClasses(attendance.getTotalClasses());
        dto.setLastUpdated(attendance.getLastUpdated());

        if (attendance.getUpdatedBy() != null) {
            dto.setUpdatedByName(attendance.getUpdatedBy().getUser().getFullName());
        }

        return dto;
    }
}
