package com.cia.management_system.dto;

import com.cia.management_system.model.Certificate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCertificateRequest {

    @NotNull(message = "Status is required")
    private Certificate.CertificateStatus status; // APPROVED or REJECTED

    private String remarks;
}
