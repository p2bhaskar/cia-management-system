package com.cia.management_system.repository;

import com.cia.management_system.model.ActivityMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityMarksRepository extends JpaRepository<ActivityMarks, Long> {

    Optional<ActivityMarks> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<ActivityMarks> findByStudentId(Long studentId);

    List<ActivityMarks> findBySubjectId(Long subjectId);

    @Query("SELECT am FROM ActivityMarks am WHERE am.student.id = :studentId")
    List<ActivityMarks> findAllByStudentId(@Param("studentId") Long studentId);
}
