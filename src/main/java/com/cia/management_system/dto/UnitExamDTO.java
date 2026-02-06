package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitExamDTO {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Integer unitNumber;
    private String title;
    private String description;
    private String googleFormUrl;
    private Integer durationMinutes;
    private Integer totalQuestions;
    private Double maxMarks;
    private LocalDateTime scheduledDate;
    private Boolean isActive;
    private Boolean isPublished;
    private Boolean isUnlocked; // For student view
    private LocalDateTime createdAt;
    private String createdByName;

    // Student-specific fields
    private Boolean hasAttempted;
    private Double marksObtained;
    private LocalDateTime attemptedAt;
    private Boolean isCompleted;

    // Faculty stats
    private Long totalAttempts;
    private Long completedAttempts;
}