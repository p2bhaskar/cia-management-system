package com.cia.management_system.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO
 * Used for user login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;
}