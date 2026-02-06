package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id; // student.id
    private Long userId; // user.id
    private String username;
    private String email;
    private String fullName;
    private String rollNumber;
    private Integer semester;
    private String section;
    private String stream;
    private String academicYear;
    private String phoneNumber;
    private String address;
    private Boolean isActive;
}