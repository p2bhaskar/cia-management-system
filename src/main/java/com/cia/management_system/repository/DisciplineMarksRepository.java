package com.cia.management_system.repository;



import com.cia.management_system.model.DisciplineMarks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineMarksRepository extends JpaRepository<DisciplineMarks, Long> {

    Optional<DisciplineMarks> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    List<DisciplineMarks> findByStudentId(Long studentId);

    List<DisciplineMarks> findBySubjectId(Long subjectId);

    @Query("SELECT COUNT(d) FROM DisciplineMarks d WHERE d.subject.id = :subjectId")
    Long countBySubjectId(@Param("subjectId") Long subjectId);
}