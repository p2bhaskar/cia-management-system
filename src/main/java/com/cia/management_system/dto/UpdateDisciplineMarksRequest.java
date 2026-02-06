package com.cia.management_system.dto;



import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateDisciplineMarksRequest {

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Marks obtained is required")
    @DecimalMin(value = "0.0", message = "Marks must be at least 0")
    @DecimalMax(value = "3.0", message = "Marks cannot exceed 3")
    private Double marksObtained;

    private String remarks;
}
