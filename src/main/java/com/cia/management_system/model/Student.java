//package com.cia.management_system.model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
///**
// * Student Entity - Extended profile for users with STUDENT role
// */
//@Entity
//@Table(name = "students",
//        uniqueConstraints = {
//                @UniqueConstraint(columnNames = "roll_number")
//        })
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Student {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = "Roll number is required")
//    @Pattern(regexp = "^\\d{12}$", message = "Roll number must be 12 digits (e.g., 240205131003)")
//    @Column(name = "roll_number", nullable = false, unique = true, length = 12)
//    private String rollNumber;
//
//    @NotBlank(message = "Phone Number is required")
//    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
//    private String phoneNumber;
//
//    @Column(nullable = false)
//    private Integer semester;
//
//    @Column(length = 10)
//    private String section; // A, B, C, etc.
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 10)
//    private Stream stream; // BCA or MCA
//
//    @Column(name = "admission_year")
//    private Integer admissionYear;
//
//    @Column(name = "academic_year", length = 20)
//    private String academicYear; // e.g., "2024-2025"
//
//    @Column(name = "current_cgpa")
//    private Double currentCgpa;
//
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @CreationTimestamp
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(length = 50)
//    private String address;
//
//    // ============================================
//    // STREAM ENUM
//    // ============================================
//    public enum Stream {
//        BCA("Bachelor of Computer Applications"),
//        MCA("Master of Computer Applications");
//
//        private final String fullName;
//
//        Stream(String fullName) {
//            this.fullName = fullName;
//        }
//
//        public String getFullName() {
//            return fullName;
//        }
//    }
//
//    // ============================================
//    // HELPER METHODS
//    // ============================================
//
//    /**
//     * Extract admission year from roll number
//     * Example: 240205131003 -> 2024
//     */
//    public void extractAdmissionYear() {
//        if (rollNumber != null && rollNumber.length() == 12) {
//            String yearPrefix = rollNumber.substring(0, 2);
//            this.admissionYear = 2000 + Integer.parseInt(yearPrefix);
//        }
//    }
//
//    /**
//     * Generate academic year from admission year
//     * Example: 2024 -> "2024-2025"
//     */
//    public void generateAcademicYear() {
//        if (admissionYear != null) {
//            this.academicYear = admissionYear + "-" + (admissionYear + 1);
//        }
//    }
//}


package com.cia.management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Student Entity - Extended profile for users with STUDENT role
 */
@Entity
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "roll_number")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Roll number is required")
    @Column(name = "roll_number", nullable = false, unique = true, length = 20)  // ✅ Increased length
    private String rollNumber;

    // ✅ FIXED: Made phone number optional for bulk import
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(nullable = false)
    private Integer semester;

    @Column(length = 10)
    private String section; // A, B, C, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Stream stream; // BCA or MCA

    @Column(name = "admission_year")
    private Integer admissionYear;

    @Column(name = "academic_year", length = 20)
    private String academicYear; // e.g., "2024-2025"

    @Column(name = "current_cgpa")
    private Double currentCgpa;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 255)  // ✅ Increased length for address
    private String address;

    // ============================================
    // STREAM ENUM
    // ============================================
    public enum Stream {
        BCA("Bachelor of Computer Applications"),
        MCA("Master of Computer Applications");

        private final String fullName;

        Stream(String fullName) {
            this.fullName = fullName;
        }

        public String getFullName() {
            return fullName;
        }
    }

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Extract admission year from roll number
     * Example: 240205131003 -> 2024
     */
    public void extractAdmissionYear() {
        if (rollNumber != null && rollNumber.length() >= 2) {
            try {
                String yearPrefix = rollNumber.substring(0, 2);
                this.admissionYear = 2000 + Integer.parseInt(yearPrefix);
            } catch (NumberFormatException e) {
                // Keep null if parsing fails
            }
        }
    }

    /**
     * Generate academic year from admission year
     * Example: 2024 -> "2024-2025"
     */
    public void generateAcademicYear() {
        if (admissionYear != null) {
            this.academicYear = admissionYear + "-" + (admissionYear + 1);
        }
    }
}