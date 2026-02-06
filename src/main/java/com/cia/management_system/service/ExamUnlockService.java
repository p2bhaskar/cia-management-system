package com.cia.management_system.service;

import com.cia.management_system.model.AssignmentSubmission;
import com.cia.management_system.repository.AssignmentSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to check if unit exams are unlocked for students
 * Logic: Unit N exam unlocks ONLY if Unit N assignment is graded
 */
@Service
@RequiredArgsConstructor
public class ExamUnlockService {

    private final AssignmentSubmissionRepository submissionRepository;

    /**
     * Check if a unit exam is unlocked for a student
     * @param studentId Student ID
     * @param subjectId Subject ID
     * @param unitNumber Unit number (1-5)
     * @return true if unlocked, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean isExamUnlocked(Long studentId, Long subjectId, Integer unitNumber) {
        // Find assignment submission for this unit
        return submissionRepository
                .findByStudentIdAndAssignmentSubjectIdAndAssignmentUnitNumber(
                        studentId, subjectId, unitNumber
                )
                .map(AssignmentSubmission::getIsGraded)
                .orElse(false); // Not unlocked if no submission or not graded
    }

    /**
     * Check unlock status for all 5 units
     */
    @Transactional(readOnly = true)
    public boolean[] getUnlockStatusForAllUnits(Long studentId, Long subjectId) {
        boolean[] unlockStatus = new boolean[5];

        for (int i = 0; i < 5; i++) {
            unlockStatus[i] = isExamUnlocked(studentId, subjectId, i + 1);
        }

        return unlockStatus;
    }
}