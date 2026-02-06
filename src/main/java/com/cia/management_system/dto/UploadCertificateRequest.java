package com.cia.management_system.dto;

import com.cia.management_system.model.Certificate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UploadCertificateRequest {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Certificate type is required")
    private Certificate.CertificateType certificateType;

    @NotBlank(message = "Certificate title is required")
    private String certificateTitle;

    @NotBlank(message = "Certificate URL is required")
    private String certificateUrl; // Cloudinary URL from frontend
}