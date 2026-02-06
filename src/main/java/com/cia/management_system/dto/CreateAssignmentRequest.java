package com.cia.management_system.dto;



import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateAssignmentRequest {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Unit number is required")
    @Min(value = 1, message = "Unit number must be between 1 and 5")
    @Max(value = 5, message = "Unit number must be between 1 and 5")
    private Integer unitNumber;

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    private String description;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be in the future")
    private LocalDateTime deadline;

    private String googleClassroomLink;
}