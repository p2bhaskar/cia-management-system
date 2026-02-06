package com.cia.management_system.dto;



import com.cia.management_system.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Signup Request DTO
 * Used for new user registration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    @Size(max = 100)
    private String fullName;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    private String role; // STUDENT, FACULTY, or ADMIN

    // Student-specific fields
    private String rollNumber; // Required if role is STUDENT
    private Integer semester;
    private String section;
    private String stream; // BCA or MCA

    // Faculty-specific fields
    private String employeeId; // Required if role is FACULTY
    private String department;
    private String designation;

    /**
     * Convert string role to Role enum
     */
    public Role getRoleEnum() {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
