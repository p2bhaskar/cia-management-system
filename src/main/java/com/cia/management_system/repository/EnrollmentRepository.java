package com.cia.management_system.repository;



import com.cia.management_system.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Get all subjects enrolled by a student
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.isActive = true")
    List<Enrollment> findByStudentId(@Param("studentId") Long studentId);

    // Get all students enrolled in a subject
    @Query("SELECT e FROM Enrollment e WHERE e.subject.id = :subjectId AND e.isActive = true")
    List<Enrollment> findBySubjectId(@Param("subjectId") Long subjectId);

    // Get all subjects taught by a faculty
    @Query("SELECT e FROM Enrollment e WHERE e.faculty.id = :facultyId AND e.isActive = true")
    List<Enrollment> findByFacultyId(@Param("facultyId") Long facultyId);

    // Get all students for a faculty's subject
    @Query("SELECT e FROM Enrollment e WHERE e.faculty.id = :facultyId AND e.subject.id = :subjectId AND e.isActive = true")
    List<Enrollment> findByFacultyIdAndSubjectId(@Param("facultyId") Long facultyId,
                                                 @Param("subjectId") Long subjectId);

    // Check if student is enrolled in subject
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.subject.id = :subjectId AND e.isActive = true")
    Optional<Enrollment> findByStudentIdAndSubjectId(@Param("studentId") Long studentId,
                                                     @Param("subjectId") Long subjectId);

    // Get enrollment by student, subject and academic year
    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.subject.id = :subjectId AND e.academicYear = :academicYear")
    Optional<Enrollment> findByStudentIdAndSubjectIdAndAcademicYear(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("academicYear") String academicYear);

    // Count students in a subject
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subject.id = :subjectId AND e.isActive = true")
    Long countStudentsBySubjectId(@Param("subjectId") Long subjectId);
}
