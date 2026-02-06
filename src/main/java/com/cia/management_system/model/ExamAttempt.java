package com.cia.management_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "student_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private UnitExam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime submittedAt;

    @Column
    private Double marksObtained; // Out of 4

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @Column
    private String googleFormResponseUrl; // Link to student's response

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime attemptedAt = LocalDateTime.now();
}