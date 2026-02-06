package com.cia.management_system.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "discipline_marks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineMarks {

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
    private Double marksObtained; // 0 to 3

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Faculty updatedBy;
}