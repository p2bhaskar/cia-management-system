package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultySubjectAssignmentDTO {
    private Long id;
    private Long facultyId;
    private String facultyName;
    private String employeeId;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String section;
    private String academicYear;
    private Boolean isActive;
    private LocalDateTime assignedAt;
}