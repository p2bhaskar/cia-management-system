package com.cia.management_system.repository;


import aj.org.objectweb.asm.commons.Remapper;
import com.cia.management_system.model.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    Optional<AssignmentSubmission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    List<AssignmentSubmission> findByAssignmentId(Long assignmentId);

    List<AssignmentSubmission> findByStudentId(Long studentId);

    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment.id = :assignmentId")
    Long countSubmissionsByAssignmentId(@Param("assignmentId") Long assignmentId);

    @Query("SELECT COUNT(s) FROM AssignmentSubmission s WHERE s.assignment.id = :assignmentId AND s.isGraded = true")
    Long countGradedSubmissionsByAssignmentId(@Param("assignmentId") Long assignmentId);

    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student.id = :studentId AND s.assignment.subject.id = :subjectId")
    List<AssignmentSubmission> findByStudentIdAndSubjectId(@Param("studentId") Long studentId,
                                                           @Param("subjectId") Long subjectId);

//    @Query("SELECT s FROM AssignmentSubmission s WHERE " +
//            "s.student.id = :studentId AND " +
//            "s.assignment.subject.id = :subjectId AND " +
//            "s.assignment.unitNumber = :unitNumber")
//    Optional<AssignmentSubmission> findByStudentIdAndAssignmentSubjectIdAndAssignmentUnitNumber(
//            @Param("studentId") Long studentId,
//            @Param("subjectId") Long subjectId,
//            @Param("unitNumber") Integer unitNumber);


    @Query("SELECT s FROM AssignmentSubmission s WHERE s.student.id = :studentId " +
            "AND s.assignment.subject.id = :subjectId AND s.assignment.unitNumber = :unitNumber")
    Optional<AssignmentSubmission> findByStudentIdAndAssignmentSubjectIdAndAssignmentUnitNumber(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("unitNumber") Integer unitNumber);
}