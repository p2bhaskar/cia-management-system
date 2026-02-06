//package com.cia.management_system.repository;
//
//
//
//import com.cia.management_system.model.Role;
//import com.cia.management_system.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * User Repository
// * Database operations for User entity
// */
//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//
//    /**
//     * Find user by username
//     */
//    Optional<User> findByUsername(String username);
//
//    /**
//     * Find user by email
//     */
//    Optional<User> findByEmail(String email);
//
//    /**
//     * Find user by username or email
//     */
//    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
//    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
//
//    /**
//     * Check if username exists
//     */
//    Boolean existsByUsername(String username);
//
//    /**
//     * Check if email exists
//     */
//    Boolean existsByEmail(String email);
//
//    /**
//     * Find all users by role
//     */
//    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
//    List<User> findByRole(@Param("role") Role role);
//
//    /**
//     * Find all enabled users
//     */
//    List<User> findByEnabledTrue();
//
//    /**
//     * Find all disabled users
//     */
//    List<User> findByEnabledFalse();
//
//    /**
//     * Count users by role
//     */
//    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = :role")
//    Long countByRole(@Param("role") Role role);
//
//
//
//
//
//
//
//    Long countByIsActiveTrue();
//}


package com.cia.management_system.repository;

import com.cia.management_system.model.Role;
import com.cia.management_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository
 * Database operations for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ===================== FIND =====================

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    // ===================== EXISTS =====================

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    // ===================== ROLE BASED =====================

    /**
     * Find users having a specific role
     */
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRolesContaining(@Param("role") Role role);

    /**
     * Count users having a specific role
     */
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r = :role")
    Long countByRolesContaining(@Param("role") Role role);

    // ===================== STATUS =====================

    List<User> findByEnabledTrue();

    List<User> findByEnabledFalse();

    Long countByEnabledTrue();
}

