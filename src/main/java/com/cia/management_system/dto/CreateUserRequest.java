package com.cia.management_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    // Student fields
    private String rollNumber;
    private Integer semester;
    private String stream;
    private String section;

    // Faculty fields
    private String employeeId;
    private String department;
    private String designation;

    // ✅ ADD THIS
    private String phoneNumber;
}