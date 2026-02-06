package com.cia.management_system.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_summary",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Double attendancePercentage; // Stored as decimal: 0.70 = 70%

    @Column(nullable = false)
    private Double marksObtained; // Out of 7: attendancePercentage × 7

    @Column
    private Integer classesAttended;

    @Column
    private Integer totalClasses;

    @Column(nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Faculty updatedBy;
}