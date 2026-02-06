package com.cia.management_system.dto;

import com.cia.management_system.model.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Certificate.CertificateType certificateType;
    private String certificateTitle;
    private String certificateUrl;
    private LocalDateTime uploadDate;
    private Certificate.CertificateStatus status;
    private BigDecimal marksAwarded;
    private Long reviewedById;
    private String reviewedByName;
    private LocalDateTime reviewedAt;
    private String remarks;
}