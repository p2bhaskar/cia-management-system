////package com.cia.management_system.dto;
////
////import lombok.AllArgsConstructor;
////import lombok.Data;
////import lombok.NoArgsConstructor;
////import java.util.ArrayList;
////import java.util.List;
////
////@Data
////@NoArgsConstructor
////@AllArgsConstructor
////public class BulkImportResult {
////    private int totalRecords;
////    private int successCount;
////    private int failureCount;
////    private List<String> errors;
////    private List<String> warnings;
////    private String message;
////
////    public BulkImportResult(int successCount, int failureCount, List<String> errors) {
////        this.totalRecords = successCount + failureCount;
////        this.successCount = successCount;
////        this.failureCount = failureCount;
////        this.errors = errors != null ? errors : new ArrayList<>();
////        this.warnings = new ArrayList<>();
////        this.message = buildMessage();
////    }
////
////    private String buildMessage() {
////        if (failureCount == 0) {
////            return String.format("Successfully imported %d records!", successCount);
////        } else if (successCount == 0) {
////            return String.format("Failed to import all %d records. Check errors.", failureCount);
////        } else {
////            return String.format("Imported %d records successfully. %d failed.", successCount, failureCount);
////        }
////    }
////
////    public void addError(String error) {
////        if (this.errors == null) {
////            this.errors = new ArrayList<>();
////        }
////        this.errors.add(error);
////    }
////
////    public void addWarning(String warning) {
////        if (this.warnings == null) {
////            this.warnings = new ArrayList<>();
////        }
////        this.warnings.add(warning);
////    }
////}
//
//
//package com.cia.management_system.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class BulkImportResult {
//    private int totalRecords;
//    private int successCount;
//    private int failureCount;
//    private List<String> errors;
//    private List<String> warnings;
//    private String message;
//
//    // ✅ NEW: Store credentials Excel file
//    private byte[] credentialsFile;
//    private boolean hasCredentialsFile;
//
//    public BulkImportResult(int successCount, int failureCount, List<String> errors) {
//        this.totalRecords = successCount + failureCount;
//        this.successCount = successCount;
//        this.failureCount = failureCount;
//        this.errors = errors != null ? errors : new ArrayList<>();
//        this.warnings = new ArrayList<>();
//        this.message = buildMessage();
//        this.hasCredentialsFile = false;
//    }
//
//    private String buildMessage() {
//        if (failureCount == 0) {
//            return String.format("Successfully imported %d records!", successCount);
//        } else if (successCount == 0) {
//            return String.format("Failed to import all %d records. Check errors.", failureCount);
//        } else {
//            return String.format("Imported %d records successfully. %d failed.", successCount, failureCount);
//        }
//    }
//
//    public void addError(String error) {
//        if (this.errors == null) {
//            this.errors = new ArrayList<>();
//        }
//        this.errors.add(error);
//    }
//
//    public void addWarning(String warning) {
//        if (this.warnings == null) {
//            this.warnings = new ArrayList<>();
//        }
//        this.warnings.add(warning);
//    }
//
//    // ✅ NEW: Set credentials file
//    public void setCredentialsFile(byte[] credentialsFile) {
//        this.credentialsFile = credentialsFile;
//        this.hasCredentialsFile = credentialsFile != null && credentialsFile.length > 0;
//    }
//}




package com.cia.management_system.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class BulkImportResult {
    private int successCount = 0;
    private int failureCount = 0;
    private int totalRecords = 0;
    private List<String> errors = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
    private byte[] credentialsFile;
    private boolean hasCredentialsFile = false;
    private String message;

    // Add this for chunk imports
    private List<Map<String, String>> credentials = new ArrayList<>();

    public BulkImportResult() {}

    public BulkImportResult(int successCount, int failureCount, List<String> errors) {
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.totalRecords = successCount + failureCount;
        this.errors = errors;
    }

    // Add getters and setters for credentials
    public void setCredentials(List<Map<String, String>> credentials) {
        this.credentials = credentials;
    }

    public List<Map<String, String>> getCredentials() {
        return credentials;
    }

    // Helper methods
    public void addError(String error) {
        this.errors.add(error);
        this.failureCount++;
        this.totalRecords++;
    }

    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    public void incrementSuccess() {
        this.successCount++;
        this.totalRecords++;
    }

    public void setHasCredentialsFile(boolean hasCredentialsFile) {
        this.hasCredentialsFile = hasCredentialsFile;
    }
}