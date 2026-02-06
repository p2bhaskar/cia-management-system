package com.cia.management_system.repository;

import com.cia.management_system.model.UnitExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UnitExamRepository extends JpaRepository<UnitExam, Long> {

    List<UnitExam> findBySubjectId(Long subjectId);

    Optional<UnitExam> findBySubjectIdAndUnitNumber(Long subjectId, Integer unitNumber);

    @Query("SELECT e FROM UnitExam e WHERE e.subject.id = :subjectId AND e.isActive = true ORDER BY e.unitNumber ASC")
    List<UnitExam> findActiveExamsBySubject(@Param("subjectId") Long subjectId);

    @Query("SELECT e FROM UnitExam e WHERE e.createdBy.id = :facultyId ORDER BY e.subject.id, e.unitNumber")
    List<UnitExam> findByCreatedById(@Param("facultyId") Long facultyId);

    boolean existsBySubjectIdAndUnitNumber(Long subjectId, Integer unitNumber);
}