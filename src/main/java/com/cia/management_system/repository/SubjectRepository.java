package com.cia.management_system.repository;


import com.cia.management_system.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Optional<Subject> findBySubjectCode(String subjectCode);

    List<Subject> findByIsActiveTrueOrderBySubjectNameAsc();

    List<Subject> findBySemesterAndStreamAndIsActiveTrue(Integer semester, String stream);

    List<Subject> findByStreamAndIsActiveTrueOrderBySemesterAsc(String stream);

    @Query("SELECT s FROM Subject s WHERE s.semester = :semester AND s.stream = :stream AND s.isActive = true")
    List<Subject> findActiveSemesterSubjects(@Param("semester") Integer semester,
                                             @Param("stream") String stream);

    @Query("SELECT s FROM Subject s WHERE s.academicYear = :academicYear AND s.isActive = true")
    List<Subject> findByAcademicYearAndIsActiveTrue(@Param("academicYear") String academicYear);

    boolean existsBySubjectCode(String subjectCode);




}
