//package com.cia.management_system.dto;
//
//
//
//import lombok.Data;
//import java.time.LocalDateTime;
//
//@Data
//public class UserDTO {
//    private Long id;
//    private String username;
//    private String email;
//    private String fullName;
//    private String role;
//    private Boolean isActive;
//    private LocalDateTime createdAt;
//
//    // Student-specific
//    private String rollNumber;
//    private Integer semester;
//    private String stream;
//    private String section;
//
//    // Faculty-specific
//    private String employeeId;
//    private String department;
//    private String designation;
//}


package com.cia.management_system.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDTO {
    // ✅ CRITICAL: id should be student.id or faculty.id
    private Long id; // This will be student.id or faculty.id

    // ✅ ADDED: Separate field for user.id
    private Long userId; // This will be user.id

    private String username;
    private String email;
    private String fullName;
    private String role;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // Student-specific
    private String rollNumber;
    private Integer semester;
    private String stream;
    private String section;
    private String academicYear;

    // Faculty-specific
    private String employeeId;
    private String department;
    private String designation;

    // ✅ ADDED: For showing generated password on creation
    private String generatedPassword;
}