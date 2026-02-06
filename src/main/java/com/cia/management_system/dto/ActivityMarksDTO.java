package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityMarksDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private BigDecimal nptelMarks;
    private BigDecimal sdpMarks;
    private BigDecimal totalMarks;
    private LocalDateTime lastUpdated;
    private String updatedByName;
}
