package com.cia.management_system.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "faculty_subject_assignments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"faculty_id", "subject_id", "academic_year", "section"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultySubjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"}) // ✅ DON'T SERIALIZE USER
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false, length = 50)
    private String academicYear; // e.g., "2024-2025"

    @Column(length = 10)
    private String section; // A, B, C, or NULL for all sections

    @Column(nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedBy; // Admin who made this assignment

    @Column(nullable = false)
    private Boolean isActive = true;
}
