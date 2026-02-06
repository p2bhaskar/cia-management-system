package com.cia.management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDTO {
    private Long id; // faculty.id
    private Long userId; // user.id
    private String username;
    private String email;
    private String fullName;
    private String employeeId;
    private String department;
    private String designation;
    private String qualification;
    private String specialization;
    private String phoneNumber;
    private String officeRoom;
    private String officeHours;
    private Boolean isActive;
}