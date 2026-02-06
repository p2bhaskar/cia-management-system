package com.cia.management_system.repository;




import com.cia.management_system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Student Repository
 * Database operations for Student entity
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find student by roll number
     */
    //Optional<Student> findByRollNumber(String rollNumber);

    /**
     * Find student by user ID
     */
   // Optional<Student> findByUserId(Long userId);

    /**
     * Check if roll number exists
     */
    //Boolean existsByRollNumber(String rollNumber);

    /**
     * Find all students by stream
     */
    List<Student> findByStream(Student.Stream stream);

    /**
     * Find all students by semester
     */
    List<Student> findBySemester(Integer semester);

    /**
     * Find all students by stream and semester
     */
    List<Student> findByStreamAndSemester(Student.Stream stream, Integer semester);

    /**
     * Find all students by section
     */
    List<Student> findBySection(String section);

    /**
     * Find all students by academic year
     */
    List<Student> findByAcademicYear(String academicYear);

    /**
     * Find students by semester and section
     */
    List<Student> findBySemesterAndSection(Integer semester, String section);

    /**
     * Count students by stream
     */
    Long countByStream(Student.Stream stream);

    /**
     * Count students by semester
     */
    Long countBySemester(Integer semester);

    /**
     * Get all distinct semesters
     */
    @Query("SELECT DISTINCT s.semester FROM Student s ORDER BY s.semester")
    List<Integer> findDistinctSemesters();

    /**
     * Get all distinct sections
     */
    @Query("SELECT DISTINCT s.section FROM Student s ORDER BY s.section")
    List<String> findDistinctSections();

    //Optional<Object> findByUserUsername(String username);



    @Query("SELECT s FROM Student s WHERE s.user.username = :username")
    Optional<Student> findByUserUsername(@Param("username") String username);

    @Query("SELECT s FROM Student s WHERE s.user.id = :userId")
    Optional<Student> findByUserId(@Param("userId") Long userId);

    Optional<Student> findByRollNumber(String rollNumber);

    boolean existsByRollNumber(String rollNumber);
}