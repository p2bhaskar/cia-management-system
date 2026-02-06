package com.cia.management_system.service;

import com.cia.management_system.dto.CreateUnitExamRequest;
import com.cia.management_system.dto.ExamAttemptDTO;
import com.cia.management_system.dto.UnitExamDTO;
import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitExamService {

    private final UnitExamRepository examRepository;
    private final ExamAttemptRepository attemptRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final ExamUnlockService unlockService;

    @Transactional
    public UnitExamDTO createExam(CreateUnitExamRequest request, String username) {
        // Check if exam already exists for this unit
        if (examRepository.existsBySubjectIdAndUnitNumber(request.getSubjectId(), request.getUnitNumber())) {
            throw new RuntimeException("Exam already exists for Unit " + request.getUnitNumber());
        }

        Faculty faculty = facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        UnitExam exam = new UnitExam();
        exam.setSubject(subject);
        exam.setUnitNumber(request.getUnitNumber());
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setGoogleFormUrl(request.getGoogleFormUrl());
        exam.setGoogleSheetId(request.getGoogleSheetId());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalQuestions(request.getTotalQuestions());
        exam.setMaxMarks(4.0); // Fixed at 4 marks
        exam.setScheduledDate(request.getScheduledDate());
        exam.setIsActive(true);
        exam.setIsPublished(false); // Not published by default
        exam.setCreatedBy(faculty);

        UnitExam saved = examRepository.save(exam);

        return convertToDTO(saved, null);
    }

    @Transactional
    public UnitExamDTO publishExam(Long examId) {
        UnitExam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.setIsPublished(true);
        UnitExam saved = examRepository.save(exam);

        return convertToDTO(saved, null);
    }

    @Transactional
    public UnitExamDTO unpublishExam(Long examId) {
        UnitExam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.setIsPublished(false);
        UnitExam saved = examRepository.save(exam);

        return convertToDTO(saved, null);
    }

    @Transactional(readOnly = true)
    public List<UnitExamDTO> getExamsBySubject(Long subjectId) {
        List<UnitExam> exams = examRepository.findBySubjectId(subjectId);

        return exams.stream()
                .map(exam -> {
                    Long totalAttempts = (long) attemptRepository.findByExamId(exam.getId()).size();
                    Long completedAttempts = attemptRepository.countCompletedAttempts(exam.getId());

                    UnitExamDTO dto = convertToDTO(exam, null);
                    dto.setTotalAttempts(totalAttempts);
                    dto.setCompletedAttempts(completedAttempts);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UnitExamDTO> getExamsForStudent(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<UnitExam> exams = examRepository.findActiveExamsBySubject(subjectId);

        return exams.stream()
                .map(exam -> {
                    // Check if unlocked
                    boolean isUnlocked = unlockService.isExamUnlocked(
                            studentId, subjectId, exam.getUnitNumber()
                    );

                    // Check if attempted
                    ExamAttempt attempt = attemptRepository
                            .findByExamIdAndStudentId(exam.getId(), studentId)
                            .orElse(null);

                    UnitExamDTO dto = convertToDTO(exam, attempt);
                    dto.setIsUnlocked(isUnlocked && exam.getIsPublished());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ExamAttemptDTO startExam(Long examId, Long studentId) {
        UnitExam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Check if exam is unlocked
        boolean isUnlocked = unlockService.isExamUnlocked(
                studentId, exam.getSubject().getId(), exam.getUnitNumber()
        );

        if (!isUnlocked || !exam.getIsPublished()) {
            throw new RuntimeException("Exam is locked. Complete and get graded for Unit " +
                    exam.getUnitNumber() + " assignment first.");
        }

        // Check if already attempted
        if (attemptRepository.existsByExamIdAndStudentId(examId, studentId)) {
            throw new RuntimeException("You have already attempted this exam");
        }

        ExamAttempt attempt = new ExamAttempt();
        attempt.setExam(exam);
        attempt.setStudent(student);
        attempt.setStartedAt(LocalDateTime.now());
        attempt.setIsCompleted(false);

        ExamAttempt saved = attemptRepository.save(attempt);

        return convertAttemptToDTO(saved);
    }

    @Transactional
    public ExamAttemptDTO markExamComplete(Long attemptId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        attempt.setSubmittedAt(LocalDateTime.now());
        attempt.setIsCompleted(true);

        ExamAttempt saved = attemptRepository.save(attempt);

        return convertAttemptToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ExamAttemptDTO> getAttemptsByExam(Long examId) {
        List<ExamAttempt> attempts = attemptRepository.findByExamId(examId);

        return attempts.stream()
                .map(this::convertAttemptToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateExamScore(Long attemptId, Double marks, String remarks) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (marks < 0 || marks > 4) {
            throw new RuntimeException("Marks must be between 0 and 4");
        }

        attempt.setMarksObtained(marks);
        attempt.setRemarks(remarks);

        attemptRepository.save(attempt);
    }

    private UnitExamDTO convertToDTO(UnitExam exam, ExamAttempt attempt) {
        UnitExamDTO dto = new UnitExamDTO();
        dto.setId(exam.getId());
        dto.setSubjectId(exam.getSubject().getId());
        dto.setSubjectName(exam.getSubject().getSubjectName());
        dto.setSubjectCode(exam.getSubject().getSubjectCode());
        dto.setUnitNumber(exam.getUnitNumber());
        dto.setTitle(exam.getTitle());
        dto.setDescription(exam.getDescription());
        dto.setGoogleFormUrl(exam.getGoogleFormUrl());
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setTotalQuestions(exam.getTotalQuestions());
        dto.setMaxMarks(exam.getMaxMarks());
        dto.setScheduledDate(exam.getScheduledDate());
        dto.setIsActive(exam.getIsActive());
        dto.setIsPublished(exam.getIsPublished());
        dto.setCreatedAt(exam.getCreatedAt());

        if (exam.getCreatedBy() != null) {
            dto.setCreatedByName(exam.getCreatedBy().getUser().getFullName());
        }

        // Student-specific fields
        if (attempt != null) {
            dto.setHasAttempted(true);
            dto.setMarksObtained(attempt.getMarksObtained());
            dto.setAttemptedAt(attempt.getAttemptedAt());
            dto.setIsCompleted(attempt.getIsCompleted());
        } else {
            dto.setHasAttempted(false);
        }

        return dto;
    }

    private ExamAttemptDTO convertAttemptToDTO(ExamAttempt attempt) {
        ExamAttemptDTO dto = new ExamAttemptDTO();
        dto.setId(attempt.getId());
        dto.setExamId(attempt.getExam().getId());
        dto.setExamTitle(attempt.getExam().getTitle());
        dto.setStudentId(attempt.getStudent().getId());
        dto.setStudentName(attempt.getStudent().getUser().getFullName());
        dto.setRollNumber(attempt.getStudent().getRollNumber());
        dto.setStartedAt(attempt.getStartedAt());
        dto.setSubmittedAt(attempt.getSubmittedAt());
        dto.setMarksObtained(attempt.getMarksObtained());
        dto.setIsCompleted(attempt.getIsCompleted());
        dto.setRemarks(attempt.getRemarks());
        dto.setAttemptedAt(attempt.getAttemptedAt());
        return dto;
    }
}