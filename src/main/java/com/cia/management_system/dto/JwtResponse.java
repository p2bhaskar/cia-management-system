//package com.cia.management_system.dto;
//
//
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
///**
// * JWT Response DTO
// * Returned after successful login
// */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class JwtResponse {
//
//    private String token;
//    private String type = "Bearer";
//    private Long id;
//    private String username;
//    private String email;
//    private String fullName;
//    private List<String> roles;
//
//    // Additional user info
//    private String rollNumber; // For students
//    private String employeeId; // For faculty
//    private Integer semester; // For students
//    private String stream; // For students
//
//    // Constructor without token type (default to "Bearer")
//    public JwtResponse(String token, Long id, String username, String email,
//                       String fullName, List<String> roles) {
//        this.token = token;
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.fullName = fullName;
//        this.roles = roles;
//    }
//
//    // Full constructor
//    public JwtResponse(String token, Long id, String username, String email,
//                       String fullName, List<String> roles, String rollNumber,
//                       String employeeId, Integer semester, String stream) {
//        this.token = token;
//        this.id = id;
//        this.username = username;
//        this.email = email;
//        this.fullName = fullName;
//        this.roles = roles;
//        this.rollNumber = rollNumber;
//        this.employeeId = employeeId;
//        this.semester = semester;
//        this.stream = stream;
//    }
//}


package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JWT Response DTO
 * Returned after successful login with complete user details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    private String type = "Bearer";

    // User object (nested structure for frontend)
    private UserDetails user;

    // Constructor with token and user details
    public JwtResponse(String token, UserDetails user) {
        this.token = token;
        this.user = user;
    }

    public JwtResponse(String jwt, Long id, String username, String email, String fullName, List<String> roles, String rollNumber, String employeeId, Integer semester, String stream) {
    }

    /**
     * Nested UserDetails class
     * Contains all user information
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDetails {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private String role; // Single role: "STUDENT", "FACULTY", or "ADMIN"

        // Student-specific fields
        private Long studentId;
        private String rollNumber;
        private Integer semester;
        private String stream;
        private String section;
        private String academicYear;

        // Faculty-specific fields
        private Long facultyId;
        private String employeeId;
        private String department;
        private String designation;
    }
}
