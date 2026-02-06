package com.cia.management_system.controller;

//
//
//import com.cia.management_system.model.Subject;
//import com.cia.management_system.service.SubjectService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/subjects")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class SubjectController {
//
//    private final SubjectService subjectService;
//
//    // Get all active subjects (accessible to all authenticated users)
//    @GetMapping
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<List<Subject>> getAllSubjects() {
//        List<Subject> subjects = subjectService.getAllActiveSubjects();
//        return ResponseEntity.ok(subjects);
//    }
//
//    // Get subject by ID
//    @GetMapping("/{id}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
//        try {
//            Subject subject = subjectService.getSubjectById(id);
//            return ResponseEntity.ok(subject);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Get subject by code
//    @GetMapping("/code/{subjectCode}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<Subject> getSubjectByCode(@PathVariable String subjectCode) {
//        try {
//            Subject subject = subjectService.getSubjectByCode(subjectCode);
//            return ResponseEntity.ok(subject);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Get subjects by semester and stream
//    @GetMapping("/semester/{semester}/stream/{stream}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<List<Subject>> getSubjectsBySemesterAndStream(
//            @PathVariable Integer semester,
//            @PathVariable String stream) {
//        List<Subject> subjects = subjectService.getSubjectsBySemesterAndStream(semester, stream);
//        return ResponseEntity.ok(subjects);
//    }
//
//    // Get subjects by stream
//    @GetMapping("/stream/{stream}")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<List<Subject>> getSubjectsByStream(@PathVariable String stream) {
//        List<Subject> subjects = subjectService.getSubjectsByStream(stream);
//        return ResponseEntity.ok(subjects);
//    }
//
//    // Create subject (Admin only)
//    @PostMapping
//   // @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
//        try {
//            Subject createdSubject = subjectService.createSubject(subject);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
//
//    // Update subject (Admin only)
//    @PutMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Subject> updateSubject(
//            @PathVariable Long id,
//            @RequestBody Subject subject) {
//        try {
//            Subject updatedSubject = subjectService.updateSubject(id, subject);
//            return ResponseEntity.ok(updatedSubject);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    // Delete subject (Admin only - soft delete)
//    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
//        try {
//            subjectService.deleteSubject(id);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}




import com.cia.management_system.model.Subject;
import com.cia.management_system.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectService subjectService;

    // NEW: Get subjects assigned to logged-in faculty
    @GetMapping("/my-subjects")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Subject>> getMySubjects(Authentication authentication) {
        List<Subject> subjects = subjectService.getSubjectsForFaculty(authentication.getName());
        return ResponseEntity.ok(subjects);
    }

    // NEW: Get subjects for faculty by academic year
    @GetMapping("/my-subjects/year/{academicYear}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Subject>> getMySubjectsByYear(
            @PathVariable String academicYear,
            Authentication authentication) {
        List<Subject> subjects = subjectService.getSubjectsForFacultyByAcademicYear(
                authentication.getName(), academicYear);
        return ResponseEntity.ok(subjects);
    }

    // Get all active subjects (accessible to all authenticated users)
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllActiveSubjects();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        try {
            Subject subject = subjectService.getSubjectById(id);
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/code/{subjectCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Subject> getSubjectByCode(@PathVariable String subjectCode) {
        try {
            Subject subject = subjectService.getSubjectByCode(subjectCode);
            return ResponseEntity.ok(subject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/semester/{semester}/stream/{stream}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Subject>> getSubjectsBySemesterAndStream(
            @PathVariable Integer semester,
            @PathVariable String stream) {
        List<Subject> subjects = subjectService.getSubjectsBySemesterAndStream(semester, stream);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/stream/{stream}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Subject>> getSubjectsByStream(@PathVariable String stream) {
        List<Subject> subjects = subjectService.getSubjectsByStream(stream);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        try {
            Subject createdSubject = subjectService.createSubject(subject);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSubject);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable Long id,
            @RequestBody Subject subject) {
        try {
            Subject updatedSubject = subjectService.updateSubject(id, subject);
            return ResponseEntity.ok(updatedSubject);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        try {
            subjectService.deleteSubject(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}