//package com.cia.management_system.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "activity_marks",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"}))
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class ActivityMarks {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "student_id", nullable = false)
//    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"})
//    private Student student;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "subject_id", nullable = false)
//    @JsonIgnoreProperties({"hibernateLazyInitializer"})
//    private Subject subject;
//
//    @Column(precision = 5, scale = 2)
//    private Double nptelMarks = 0.0; // Max 6 (2 certificates × 3 marks)
//
//    @Column(precision = 5, scale = 2)
//    private Double sdpMarks = 0.0; // Max 4 (2 programs × 2 marks)
//
//    @Column(precision = 5, scale = 2)
//    private Double totalMarks = 0.0; // Max 10
//
//    @Column
//    private LocalDateTime lastUpdated = LocalDateTime.now();
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "updated_by")
//    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"})
//    private Faculty updatedBy;
//}


package com.cia.management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "activity_marks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ActivityMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"})
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private Subject subject;

    // Max 6 (2 certificates × 3 marks)
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal nptelMarks = BigDecimal.ZERO;

    // Max 4 (2 programs × 2 marks)
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal sdpMarks = BigDecimal.ZERO;

    // Max 10
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal totalMarks = BigDecimal.ZERO;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"})
    private Faculty updatedBy;
}

