package com.cia.management_system.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarkSubmissionRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private String submissionFileUrl;
}
