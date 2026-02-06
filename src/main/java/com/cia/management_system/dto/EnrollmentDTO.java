package com.cia.management_system.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Long facultyId;
    private String facultyName;
    private String academicYear;
    private LocalDateTime enrolledAt;
    private Boolean isActive;
}