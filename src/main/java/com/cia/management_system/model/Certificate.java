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
//@Table(name = "certificates")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//public class Certificate {
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
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 20)
//    private CertificateType certificateType;
//
//    @Column(nullable = false, length = 200)
//    private String certificateTitle;
//
//    @Column(nullable = false, length = 500)
//    private String certificateUrl; // Cloudinary URL
//
//    @Column(nullable = false)
//    private LocalDateTime uploadDate = LocalDateTime.now();
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false, length = 20)
//    private CertificateStatus status = CertificateStatus.PENDING;
//
//    @Column(precision = 5, scale = 2)
//    private Double marksAwarded = 0.0;
//
//
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "reviewed_by")
//    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"})
//    private Faculty reviewedBy;
//
//    @Column
//    private LocalDateTime reviewedAt;
//
//    @Column(columnDefinition = "TEXT")
//    private String remarks;
//
//    public enum CertificateType {
//        NPTEL,  // 3 marks each, max 2
//        SDP     // 2 marks each, max 2
//    }
//
//    public enum CertificateStatus {
//        PENDING,
//        APPROVED,
//        REJECTED
//    }
//}


package com.cia.management_system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Certificate {

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CertificateType certificateType;

    @Column(nullable = false, length = 200)
    private String certificateTitle;

    @Column(nullable = false, length = 500)
    private String certificateUrl; // Cloudinary URL

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CertificateStatus status = CertificateStatus.PENDING;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal marksAwarded = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    @JsonIgnoreProperties({"user", "hibernateLazyInitializer"})
    private Faculty reviewedBy;

    @Column
    private LocalDateTime reviewedAt;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    public enum CertificateType {
        NPTEL,  // 3 marks each, max 2
        SDP     // 2 marks each, max 2
    }

    public enum CertificateStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
