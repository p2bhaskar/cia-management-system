package com.cia.management_system.repository;

import com.cia.management_system.model.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {

    Optional<ExamAttempt> findByExamIdAndStudentId(Long examId, Long studentId);

    List<ExamAttempt> findByStudentId(Long studentId);

    List<ExamAttempt> findByExamId(Long examId);

    @Query("SELECT a FROM ExamAttempt a WHERE a.exam.subject.id = :subjectId AND a.student.id = :studentId")
    List<ExamAttempt> findBySubjectIdAndStudentId(@Param("subjectId") Long subjectId, @Param("studentId") Long studentId);

    @Query("SELECT COUNT(a) FROM ExamAttempt a WHERE a.exam.id = :examId AND a.isCompleted = true")
    Long countCompletedAttempts(@Param("examId") Long examId);

    boolean existsByExamIdAndStudentId(Long examId, Long studentId);
}