package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDTO {
    private Long id;
    private Long subjectId;
    private String subjectName;
    private Integer unitNumber;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Double maxMarks;
    private String googleClassroomLink;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String createdByName;

    // Statistics
    private Long totalSubmissions;
    private Long gradedSubmissions;

    // NEW: Student-specific fields
    private Boolean isSubmitted = false;
    private Long submissionId;
    private LocalDateTime submittedAt;
    private Boolean isGraded = false;
    private Double marksObtained;
}