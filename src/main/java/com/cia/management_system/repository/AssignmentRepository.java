package com.cia.management_system.repository;



import com.cia.management_system.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findBySubjectIdAndIsActiveTrue(Long subjectId);

    List<Assignment> findBySubjectIdOrderByUnitNumberAsc(Long subjectId);

    @Query("SELECT a FROM Assignment a WHERE a.subject.id = :subjectId AND a.unitNumber = :unitNumber")
    Assignment findBySubjectIdAndUnitNumber(@Param("subjectId") Long subjectId,
                                            @Param("unitNumber") Integer unitNumber);

    @Query("SELECT a FROM Assignment a WHERE a.createdBy.id = :facultyId ORDER BY a.createdAt DESC")
    List<Assignment> findByFacultyId(@Param("facultyId") Long facultyId);
}
