package com.cia.management_system.repository;



import com.cia.management_system.model.FacultySubjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultySubjectAssignmentRepository extends JpaRepository<FacultySubjectAssignment, Long> {

    // Get all subjects assigned to a faculty
    @Query("SELECT fsa FROM FacultySubjectAssignment fsa WHERE fsa.faculty.id = :facultyId AND fsa.isActive = true")
    List<FacultySubjectAssignment> findByFacultyId(@Param("facultyId") Long facultyId);

    // Get all subjects assigned to a faculty for current academic year
    @Query("SELECT fsa FROM FacultySubjectAssignment fsa WHERE fsa.faculty.id = :facultyId AND fsa.academicYear = :academicYear AND fsa.isActive = true")
    List<FacultySubjectAssignment> findByFacultyIdAndAcademicYear(
            @Param("facultyId") Long facultyId,
            @Param("academicYear") String academicYear);

    // Get all faculties teaching a subject
    @Query("SELECT fsa FROM FacultySubjectAssignment fsa WHERE fsa.subject.id = :subjectId AND fsa.isActive = true")
    List<FacultySubjectAssignment> findBySubjectId(@Param("subjectId") Long subjectId);

    // Check if faculty is assigned to a subject
    @Query("SELECT COUNT(fsa) > 0 FROM FacultySubjectAssignment fsa WHERE fsa.faculty.id = :facultyId AND fsa.subject.id = :subjectId AND fsa.isActive = true")
    boolean existsByFacultyIdAndSubjectId(@Param("facultyId") Long facultyId, @Param("subjectId") Long subjectId);

    // ADD THIS NEW METHOD
    @Query("SELECT fsa FROM FacultySubjectAssignment fsa " +
            "JOIN FETCH fsa.subject " +
            "WHERE fsa.faculty.id = :facultyId AND fsa.isActive = true")
    List<FacultySubjectAssignment> findByFacultyIdWithSubjects(@Param("facultyId") Long facultyId);





@Query("SELECT fsa FROM FacultySubjectAssignment fsa WHERE fsa.faculty.id = :facultyId AND fsa.isActive = true")
List<FacultySubjectAssignment> findActiveByFacultyId(@Param("facultyId") Long facultyId);

@Query("SELECT fsa FROM FacultySubjectAssignment fsa WHERE fsa.subject.id = :subjectId AND fsa.isActive = true")
List<FacultySubjectAssignment> findActiveBySubjectId(@Param("subjectId") Long subjectId);

Optional<FacultySubjectAssignment> findByFacultyIdAndSubjectIdAndSectionAndAcademicYear(
        Long facultyId, Long subjectId, String section, String academicYear);

boolean existsByFacultyIdAndSubjectIdAndSectionAndAcademicYear(
        Long facultyId, Long subjectId, String section, String academicYear);


}
