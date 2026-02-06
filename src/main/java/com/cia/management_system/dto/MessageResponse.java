package com.cia.management_system.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Message Response DTO
 * Generic response for success/error messages
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private String message;
    private boolean success;

    // Constructor with just message (default success = true)
    public MessageResponse(String message) {
        this.message = message;
        this.success = true;
    }

    // Static factory methods
    public static MessageResponse success(String message) {
        return new MessageResponse(message, true);
    }

    public static MessageResponse error(String message) {
        return new MessageResponse(message, false);
    }
}
