package com.cia.management_system.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User Entity - Core authentication entity
 * Supports three roles: STUDENT, FACULTY, ADMIN
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 60, max = 120) // BCrypt hash length
    @Column(nullable = false, length = 120)
    @JsonIgnore // ✅ NEVER SERIALIZE PASSWORD
    private String password;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // ============================================
    // RELATIONSHIPS
    // ============================================

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Student student;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Faculty faculty;

    // ============================================
    // HELPER METHODS
    // ============================================

    /**
     * Add a role to user
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }

    /**
     * Check if user has specific role
     */
    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    /**
     * Get primary role (first role in set)
     */
    public Role getPrimaryRole() {
        return this.roles.isEmpty() ? null : this.roles.iterator().next();
    }

    /**
     * Update last login timestamp
     */
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
}
