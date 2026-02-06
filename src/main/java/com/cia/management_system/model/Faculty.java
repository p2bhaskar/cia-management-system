package com.cia.management_system.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Faculty Entity - Extended profile for users with FACULTY role
 */
@Entity
@Table(name = "faculty",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "employee_id")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Employee ID is required")
    @Column(name = "employee_id", nullable = false, unique = true, length = 20)
    private String employeeId;

    @NotBlank(message = "Phone Number is required")
    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(length = 100)
    private String designation; // Professor, Associate Professor, Assistant Professor

    @Column(length = 200)
    private String specialization;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(length = 200)
    private String qualification; // PhD, M.Tech, etc.

    @Column(name = "office_room", length = 50)
    private String officeRoom;

    @Column(name = "office_hours", length = 100)
    private String officeHours; // e.g., "Mon-Fri 2:00 PM - 4:00 PM"

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"faculty", "student", "password"}) // ✅ PREVENT RECURSION
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Get full designation with department
     */
    public String getFullDesignation() {
        return (designation != null ? designation : "Faculty") +
                ", " +
                (department != null ? department : "");
    }
}
