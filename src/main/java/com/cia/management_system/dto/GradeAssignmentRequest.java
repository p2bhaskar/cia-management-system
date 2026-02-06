package com.cia.management_system.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GradeAssignmentRequest {

    @NotNull(message = "Marks obtained is required")
    @DecimalMin(value = "0.0", message = "Marks must be at least 0")
    @DecimalMax(value = "2.0", message = "Marks cannot exceed 2")
    private Double marksObtained;

    private String facultyRemarks;
}
