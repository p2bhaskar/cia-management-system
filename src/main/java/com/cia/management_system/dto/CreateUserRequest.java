package com.cia.management_system.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Role is required")
    private String role; // STUDENT, FACULTY, ADMIN

    // Student-specific fields
    private String rollNumber;
    private Integer semester;
    private String stream; // BCA, MCA
    private String section;
    private String academicYear;

    // Faculty-specific fields
    private String employeeId;
    private String department;
    private String designation;
}
