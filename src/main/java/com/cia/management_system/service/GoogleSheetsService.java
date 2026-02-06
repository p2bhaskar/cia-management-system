package com.cia.management_system.service;

import com.cia.management_system.model.ExamAttempt;
import com.cia.management_system.model.Student;
import com.cia.management_system.model.UnitExam;
import com.cia.management_system.repository.ExamAttemptRepository;
import com.cia.management_system.repository.StudentRepository;
import com.cia.management_system.repository.UnitExamRepository;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Google Sheets Service
 * Imports exam scores from Google Forms responses sheet
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleSheetsService {

    private static final String APPLICATION_NAME = "CIA Management System";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    @Value("${google.credentials.file.path:#{null}}")
    private String credentialsFilePath;

    private final UnitExamRepository examRepository;
    private final ExamAttemptRepository attemptRepository;
    private final StudentRepository studentRepository;

    /**
     * Import scores from Google Sheets
     * Expected format:
     * Column A: Timestamp
     * Column B: Email address
     * Column C: Roll Number (or Name)
     * Column D onwards: Question responses
     * Last Column: Score
     */
    @Transactional
    public ScoreImportResult importScoresFromSheet(Long examId, String sheetId, String range) {
        log.info("Starting score import for exam ID: {}", examId);

        UnitExam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ScoreImportResult result = new ScoreImportResult();

        try {
            // Build Sheets service
            Sheets sheetsService = getSheetsService();

            // Fetch data from sheet
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(sheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();

            if (values == null || values.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("No data found in sheet");
                return result;
            }

            int processedCount = 0;
            int errorCount = 0;
            List<String> errors = new ArrayList<>();

            // Skip header row, start from index 1
            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);

                try {
                    // Extract data (adjust column indices based on your form)
                    String rollNumber = getStringValue(row, 2); // Column C (index 2)
                    String scoreStr = getStringValue(row, row.size() - 1); // Last column

                    if (rollNumber == null || rollNumber.trim().isEmpty()) {
                        errors.add("Row " + (i + 1) + ": Missing roll number");
                        errorCount++;
                        continue;
                    }

                    // Find student by roll number
                    Student student = studentRepository.findByRollNumber(rollNumber.trim())
                            .orElse(null);

                    if (student == null) {
                        errors.add("Row " + (i + 1) + ": Student not found with roll number: " + rollNumber);
                        errorCount++;
                        continue;
                    }

                    // Parse score
                    double score = parseScore(scoreStr);

                    // Normalize score to 4 marks (if form gives score out of 40)
                    if (score > 4) {
                        score = (score / 40.0) * 4.0; // Convert from 40 to 4
                    }

                    // Round to 2 decimal places
                    score = Math.round(score * 100.0) / 100.0;

                    // Create or update exam attempt
                    ExamAttempt attempt = attemptRepository
                            .findByExamIdAndStudentId(examId, student.getId())
                            .orElse(new ExamAttempt());

                    attempt.setExam(exam);
                    attempt.setStudent(student);
                    attempt.setMarksObtained(score);
                    attempt.setIsCompleted(true);

                    if (attempt.getStartedAt() == null) {
                        attempt.setStartedAt(LocalDateTime.now());
                    }

                    attempt.setSubmittedAt(LocalDateTime.now());

                    attemptRepository.save(attempt);
                    processedCount++;

                    log.debug("Processed score for roll number: {}, score: {}", rollNumber, score);

                } catch (Exception e) {
                    errors.add("Row " + (i + 1) + ": " + e.getMessage());
                    errorCount++;
                    log.error("Error processing row {}: {}", i + 1, e.getMessage());
                }
            }

            result.setSuccess(true);
            result.setProcessedRecords(processedCount);
            result.setErrorCount(errorCount);
            result.setErrors(errors);
            result.setMessage(String.format("Imported %d scores successfully. %d errors.",
                    processedCount, errorCount));

            log.info("Score import complete: {} processed, {} errors", processedCount, errorCount);

        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("Failed to import scores: " + e.getMessage());
            log.error("Score import failed", e);
        }

        return result;
    }

    /**
     * Get Sheets service instance
     */
    private Sheets getSheetsService() throws Exception {
        // If credentials file is not configured, use mock service for development
        if (credentialsFilePath == null || credentialsFilePath.isEmpty()) {
            log.warn("Google credentials not configured. Using mock service.");
            throw new RuntimeException("Google Sheets integration not configured. " +
                    "Please set google.credentials.file.path in application.properties");
        }

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(credentialsFilePath))
                .createScoped(SCOPES);

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Get string value from row at index
     */
    private String getStringValue(List<Object> row, int index) {
        if (row == null || index >= row.size()) {
            return null;
        }
        Object value = row.get(index);
        return value != null ? value.toString() : null;
    }

    /**
     * Parse score from string
     */
    private double parseScore(String scoreStr) {
        if (scoreStr == null || scoreStr.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Remove any non-numeric characters except decimal point
            String cleaned = scoreStr.replaceAll("[^0-9.]", "");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Result class for score import
     */
    @lombok.Data
    public static class ScoreImportResult {
        private boolean success;
        private String message;
        private int processedRecords;
        private int errorCount;
        private List<String> errors = new ArrayList<>();
    }
}