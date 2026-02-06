package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptDTO {
    private Long id;
    private Long examId;
    private String examTitle;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private Double marksObtained;
    private Boolean isCompleted;
    private String remarks;
    private LocalDateTime attemptedAt;
}