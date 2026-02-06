package com.cia.management_system.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subjects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String subjectCode; // e.g., "CS301", "BCA201"

    @Column(nullable = false, length = 200)
    private String subjectName; // e.g., "Data Structures", "Database Management"

    @Column(nullable = false)
    private Integer semester; // 1 to 6

    @Column(nullable = false, length = 10)
    private String stream; // BCA or MCA

    @Column(nullable = false)
    private Integer credits; // 3 or 4

    @Column(length = 100)
    private String department; // Computer Science & Engineering

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 50)
    private String academicYear; // e.g., "2024-2025"
}
