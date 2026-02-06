//package com.cia.management_system.service;
//
//
//
//import com.cia.management_system.model.Faculty;
//import com.cia.management_system.model.FacultySubjectAssignment;
//import com.cia.management_system.model.Subject;
//import com.cia.management_system.model.User;
//import com.cia.management_system.repository.FacultyRepository;
//import com.cia.management_system.repository.FacultySubjectAssignmentRepository;
//import com.cia.management_system.repository.SubjectRepository;
//import com.cia.management_system.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FacultySubjectAssignmentService {
//
//    private final FacultySubjectAssignmentRepository assignmentRepository;
//    private final FacultyRepository facultyRepository;
//    private final SubjectRepository subjectRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public FacultySubjectAssignment assignSubjectToFaculty(
//            Long facultyId,
//            Long subjectId,
//            String academicYear,
//            String section,
//            String adminUsername) {
//
//        Faculty faculty = facultyRepository.findById(facultyId)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        Subject subject = subjectRepository.findById(subjectId)
//                .orElseThrow(() -> new RuntimeException("Subject not found"));
//
//        User admin = userRepository.findByUsername(adminUsername)
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        FacultySubjectAssignment assignment = new FacultySubjectAssignment();
//        assignment.setFaculty(faculty);
//        assignment.setSubject(subject);
//        assignment.setAcademicYear(academicYear);
//        assignment.setSection(section);
//        assignment.setAssignedBy(admin);
//        assignment.setAssignedAt(LocalDateTime.now());
//        assignment.setIsActive(true);
//
//        return assignmentRepository.save(assignment);
//    }
//
//    @Transactional(readOnly = true)
//    public List<FacultySubjectAssignment> getAssignmentsByFaculty(Long facultyId) {
//        return assignmentRepository.findByFacultyId(facultyId);
//    }
//
//    @Transactional(readOnly = true)
//    public List<FacultySubjectAssignment> getAssignmentsBySubject(Long subjectId) {
//        return assignmentRepository.findBySubjectId(subjectId);
//    }
//
//    @Transactional
//    public void removeAssignment(Long assignmentId) {
//        FacultySubjectAssignment assignment = assignmentRepository.findById(assignmentId)
//                .orElseThrow(() -> new RuntimeException("Assignment not found"));
//        assignment.setIsActive(false);
//        assignmentRepository.save(assignment);
//    }
//}


package com.cia.management_system.service;

import com.cia.management_system.dto.AssignSubjectRequest;
import com.cia.management_system.dto.FacultySubjectAssignmentDTO;
import com.cia.management_system.model.Faculty;
import com.cia.management_system.model.FacultySubjectAssignment;
import com.cia.management_system.model.Subject;
import com.cia.management_system.model.User;
import com.cia.management_system.repository.FacultyRepository;
import com.cia.management_system.repository.FacultySubjectAssignmentRepository;
import com.cia.management_system.repository.SubjectRepository;
import com.cia.management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultySubjectAssignmentService {

    private final FacultySubjectAssignmentRepository assignmentRepository;
    private final FacultyRepository facultyRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    @Transactional
    public FacultySubjectAssignmentDTO assignSubject(AssignSubjectRequest request, String adminUsername) {
        // Check if already assigned
        boolean exists = assignmentRepository.existsByFacultyIdAndSubjectIdAndSectionAndAcademicYear(
                request.getFacultyId(),
                request.getSubjectId(),
                request.getSection(),
                request.getAcademicYear());

        if (exists) {
            throw new RuntimeException("Subject already assigned to this faculty for this section");
        }

        Faculty faculty = facultyRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        FacultySubjectAssignment assignment = new FacultySubjectAssignment();
        assignment.setFaculty(faculty);
        assignment.setSubject(subject);
        assignment.setSection(request.getSection());
        assignment.setAcademicYear(request.getAcademicYear());
        assignment.setIsActive(true);
        assignment.setAssignedBy(admin);

        FacultySubjectAssignment saved = assignmentRepository.save(assignment);

        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<FacultySubjectAssignmentDTO> getAssignmentsByFaculty(Long facultyId) {
        List<FacultySubjectAssignment> assignments = assignmentRepository.findActiveByFacultyId(facultyId);
        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacultySubjectAssignmentDTO> getAssignmentsBySubject(Long subjectId) {
        List<FacultySubjectAssignment> assignments = assignmentRepository.findActiveBySubjectId(subjectId);
        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeAssignment(Long assignmentId) {
        FacultySubjectAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        assignment.setIsActive(false);
        assignmentRepository.save(assignment);
    }

    // ✅ CRITICAL: Convert to DTO to avoid circular references
    private FacultySubjectAssignmentDTO convertToDTO(FacultySubjectAssignment assignment) {
        FacultySubjectAssignmentDTO dto = new FacultySubjectAssignmentDTO();
        dto.setId(assignment.getId());
        dto.setFacultyId(assignment.getFaculty().getId());
        dto.setFacultyName(assignment.getFaculty().getUser().getFullName());
        dto.setEmployeeId(assignment.getFaculty().getEmployeeId());
        dto.setSubjectId(assignment.getSubject().getId());
        dto.setSubjectName(assignment.getSubject().getSubjectName());
        dto.setSubjectCode(assignment.getSubject().getSubjectCode());
        dto.setSection(assignment.getSection());
        dto.setAcademicYear(assignment.getAcademicYear());
        dto.setIsActive(assignment.getIsActive());
        dto.setAssignedAt(assignment.getAssignedAt());
        return dto;
    }
}
