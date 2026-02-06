package com.cia.management_system.repository;




import com.cia.management_system.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Faculty Repository
 * Database operations for Faculty entity
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    /**
     * Find faculty by employee ID
     */
   // Optional<Faculty> findByEmployeeId(String employeeId);

    /**
     * Find faculty by user ID
     */
    //Optional<Faculty> findByUserId(Long userId);

    /**
     * Check if employee ID exists
     */
   // Boolean existsByEmployeeId(String employeeId);

    /**
     * Find all faculty by department
     */
    List<Faculty> findByDepartment(String department);

    /**
     * Find all faculty by designation
     */
    List<Faculty> findByDesignation(String designation);

    /**
     * Count faculty by department
     */
    Long countByDepartment(String department);

  //  Optional<Object> findByUserUsername(String username);


    @Query("SELECT f FROM Faculty f WHERE f.user.username = :username")
    Optional<Faculty> findByUserUsername(@Param("username") String username);

    @Query("SELECT f FROM Faculty f WHERE f.user.id = :userId")
    Optional<Faculty> findByUserId(@Param("userId") Long userId);

    Optional<Faculty> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);
}
