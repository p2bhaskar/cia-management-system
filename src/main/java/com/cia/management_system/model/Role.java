package com.cia.management_system.model;



/**
 * Role Enum - Defines user roles in the system
 *
 * STUDENT: Can view their CIA scores, submit certificates, take exams
 * FACULTY: Can grade assignments, upload attendance, manage exams
 * ADMIN: Full system access, user management, analytics
 */
public enum Role {
    STUDENT("ROLE_STUDENT"),
    FACULTY("ROLE_FACULTY"),
    ADMIN("ROLE_ADMIN");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    /**
     * Get role from authority string
     * Example: "ROLE_STUDENT" -> Role.STUDENT
     */
    public static Role fromAuthority(String authority) {
        for (Role role : Role.values()) {
            if (role.authority.equals(authority)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown authority: " + authority);
    }

    @Override
    public String toString() {
        return this.authority;
    }
}
