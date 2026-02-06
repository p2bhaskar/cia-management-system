package com.cia.management_system.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateUnitExamRequest {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Unit number is required")
    @Min(value = 1, message = "Unit number must be between 1 and 5")
    @Max(value = 5, message = "Unit number must be between 1 and 5")
    private Integer unitNumber;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Google Form URL is required")
    private String googleFormUrl;

    private String googleSheetId;

    private Integer durationMinutes = 20;

    private Integer totalQuestions = 40;

    private LocalDateTime scheduledDate;
}