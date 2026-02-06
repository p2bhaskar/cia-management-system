package com.cia.management_system.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "unit_exams",
        uniqueConstraints = @UniqueConstraint(columnNames = {"subject_id", "unit_number"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private Integer unitNumber; // 1-5

    @Column(nullable = false, length = 200)
    private String title; // e.g., "Unit 1 Exam - Data Structures"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String googleFormUrl; // Link to Google Form

    @Column
    private String googleSheetId; // For importing scores

    @Column(nullable = false)
    private Integer durationMinutes = 20; // Default 20 minutes

    @Column(nullable = false)
    private Integer totalQuestions = 40; // 40 MCQs

    @Column(nullable = false)
    private Double maxMarks = 4.0; // Out of 4

    @Column
    private LocalDateTime scheduledDate;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isPublished = false; // Manually published by faculty

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Faculty createdBy;
}
