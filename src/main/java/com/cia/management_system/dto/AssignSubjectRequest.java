package com.cia.management_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignSubjectRequest {

    @NotNull(message = "Faculty ID is required")
    private Long facultyId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    private String section; // Optional

    private String academicYear = "2024-2025"; // Default
}