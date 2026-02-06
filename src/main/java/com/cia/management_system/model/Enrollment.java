package com.cia.management_system.model;





import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id", "academic_year"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty; // Which faculty teaches this subject to this student

    @Column(nullable = false, length = 50)
    private String academicYear; // e.g., "2024-2025"

    @Column(length = 10)
    private String section; // A, B, C (if applicable)

    @Column(nullable = false)
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean isActive = true;
}