package com.cia.management_system.repository;



import com.cia.management_system.model.AttendanceSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSummaryRepository extends JpaRepository<AttendanceSummary, Long> {

    Optional<AttendanceSummary> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<AttendanceSummary> findByStudentId(Long studentId);

    List<AttendanceSummary> findBySubjectId(Long subjectId);

    @Query("SELECT a FROM AttendanceSummary a WHERE a.student.rollNumber = :rollNumber AND a.subject.id = :subjectId")
    Optional<AttendanceSummary> findByRollNumberAndSubjectId(
            @Param("rollNumber") String rollNumber,
            @Param("subjectId") Long subjectId);

    @Query("SELECT COUNT(a) FROM AttendanceSummary a WHERE a.subject.id = :subjectId")
    Long countBySubjectId(@Param("subjectId") Long subjectId);
}
