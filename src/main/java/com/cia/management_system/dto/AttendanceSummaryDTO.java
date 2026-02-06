package com.cia.management_system.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummaryDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long subjectId;
    private String subjectName;
    private Double attendancePercentage;
    private Double marksObtained;
    private Integer classesAttended;
    private Integer totalClasses;
    private LocalDateTime lastUpdated;
    private String updatedByName;
}