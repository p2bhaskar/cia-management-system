package com.cia.management_system.controller;



import com.cia.management_system.dto.DisciplineMarksDTO;
import com.cia.management_system.dto.UpdateDisciplineMarksRequest;
import com.cia.management_system.service.DisciplineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discipline")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DisciplineController {

    private final DisciplineService disciplineService;

    // Faculty: Update discipline marks
    @PutMapping("/subject/{subjectId}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<DisciplineMarksDTO> updateDisciplineMarks(
            @PathVariable Long subjectId,
            @Valid @RequestBody UpdateDisciplineMarksRequest request,
            Authentication authentication) {
        try {
            DisciplineMarksDTO discipline = disciplineService.updateDisciplineMarks(
                    subjectId, request, authentication.getName());
            return ResponseEntity.ok(discipline);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get discipline marks by subject
    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN')")
    public ResponseEntity<List<DisciplineMarksDTO>> getDisciplineBySubject(@PathVariable Long subjectId) {
        List<DisciplineMarksDTO> discipline = disciplineService.getDisciplineMarksBySubject(subjectId);
        return ResponseEntity.ok(discipline);
    }

    // Get discipline marks by student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('FACULTY', 'ADMIN', 'STUDENT')")
    public ResponseEntity<List<DisciplineMarksDTO>> getDisciplineByStudent(@PathVariable Long studentId) {
        List<DisciplineMarksDTO> discipline = disciplineService.getDisciplineMarksByStudent(studentId);
        return ResponseEntity.ok(discipline);
    }
}
