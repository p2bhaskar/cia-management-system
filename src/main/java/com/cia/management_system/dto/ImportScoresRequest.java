package com.cia.management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImportScoresRequest {

    @NotBlank(message = "Google Sheet ID is required")
    private String googleSheetId;

    private String range = "Form Responses 1!A2:Z"; // Default range
}