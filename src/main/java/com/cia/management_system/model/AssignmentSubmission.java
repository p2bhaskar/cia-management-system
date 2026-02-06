package com.cia.management_system.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignment_submissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "student_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(length = 500)
    private String submissionFileUrl; // Google Classroom file URL

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @Column
    private Double marksObtained; // 0 to 2 (can be decimal like 1.5)

    @Column
    private Boolean isLate = false;

    @Column
    private Boolean isGraded = false;

    @Column(columnDefinition = "TEXT")
    private String facultyRemarks;

    @Column
    private LocalDateTime gradedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private Faculty gradedBy;
}
