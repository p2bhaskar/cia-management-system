//package com.cia.management_system.service;
//
//
//import com.cia.management_system.dto.*;
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AssignmentService {
//
//    private final AssignmentRepository assignmentRepository;
//    private final AssignmentSubmissionRepository submissionRepository;
//    private final SubjectRepository subjectRepository;
//    private final StudentRepository studentRepository;
//    private final FacultyRepository facultyRepository;
//
//    @Transactional
//    public AssignmentDTO createAssignment(CreateAssignmentRequest request, String username) {
//        // Get faculty
//        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        // Get subject
//        Subject subject = subjectRepository.findById(request.getSubjectId())
//                .orElseThrow(() -> new RuntimeException("Subject not found"));
//
//        // Check if assignment already exists for this unit
//        Assignment existingAssignment = assignmentRepository
//                .findBySubjectIdAndUnitNumber(request.getSubjectId(), request.getUnitNumber());
//
//        if (existingAssignment != null) {
//            throw new RuntimeException("Assignment already exists for Unit " + request.getUnitNumber());
//        }
//
//        // Create assignment
//        Assignment assignment = new Assignment();
//        assignment.setSubject(subject);
//        assignment.setUnitNumber(request.getUnitNumber());
//        assignment.setTitle(request.getTitle());
//        assignment.setDescription(request.getDescription());
//        assignment.setDeadline(request.getDeadline());
//        assignment.setMaxMarks(2.0);
//        assignment.setGoogleClassroomLink(request.getGoogleClassroomLink());
//        assignment.setCreatedBy(faculty);
//        assignment.setCreatedAt(LocalDateTime.now());
//        assignment.setIsActive(true);
//
//        Assignment savedAssignment = assignmentRepository.save(assignment);
//
//        return convertToDTO(savedAssignment);
//    }
//
//    @Transactional(readOnly = true)
//    public List<AssignmentDTO> getAssignmentsBySubject(Long subjectId) {
//        List<Assignment> assignments = assignmentRepository.findBySubjectIdOrderByUnitNumberAsc(subjectId);
//
//        return assignments.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<AssignmentDTO> getAssignmentsByFaculty(String username) {
//        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        List<Assignment> assignments = assignmentRepository.findByFacultyId(faculty.getId());
//
//        return assignments.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public AssignmentSubmissionDTO markSubmission(Long assignmentId, MarkSubmissionRequest request) {
//        Assignment assignment = assignmentRepository.findById(assignmentId)
//                .orElseThrow(() -> new RuntimeException("Assignment not found"));
//
//        Student student = studentRepository.findById(request.getStudentId())
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        // Check if submission already exists
//        AssignmentSubmission submission = submissionRepository
//                .findByAssignmentIdAndStudentId(assignmentId, student.getId())
//                .orElse(new AssignmentSubmission());
//
//        submission.setAssignment(assignment);
//        submission.setStudent(student);
//        submission.setSubmissionFileUrl(request.getSubmissionFileUrl());
//        submission.setSubmittedAt(LocalDateTime.now());
//
//        // Check if late
//        submission.setIsLate(LocalDateTime.now().isAfter(assignment.getDeadline()));
//
//        AssignmentSubmission savedSubmission = submissionRepository.save(submission);
//
//        return convertToSubmissionDTO(savedSubmission);
//    }
//
//    @Transactional
//    public AssignmentSubmissionDTO gradeSubmission(Long submissionId, GradeAssignmentRequest request, String username) {
//        AssignmentSubmission submission = submissionRepository.findById(submissionId)
//                .orElseThrow(() -> new RuntimeException("Submission not found"));
//
//        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        submission.setMarksObtained(request.getMarksObtained());
//        submission.setFacultyRemarks(request.getFacultyRemarks());
//        submission.setIsGraded(true);
//        submission.setGradedAt(LocalDateTime.now());
//        submission.setGradedBy(faculty);
//
//        AssignmentSubmission gradedSubmission = submissionRepository.save(submission);
//
//        // TODO: Trigger CIA score recalculation
//
//        return convertToSubmissionDTO(gradedSubmission);
//    }
//
//    @Transactional(readOnly = true)
//    public List<AssignmentSubmissionDTO> getSubmissionsByAssignment(Long assignmentId) {
//        List<AssignmentSubmission> submissions = submissionRepository.findByAssignmentId(assignmentId);
//
//        return submissions.stream()
//                .map(this::convertToSubmissionDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<AssignmentSubmissionDTO> getSubmissionsByStudent(String username) {
//        Student student = (Student) studentRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        List<AssignmentSubmission> submissions = submissionRepository.findByStudentId(student.getId());
//
//        return submissions.stream()
//                .map(this::convertToSubmissionDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Helper methods
//    private AssignmentDTO convertToDTO(Assignment assignment) {
//        AssignmentDTO dto = new AssignmentDTO();
//        dto.setId(assignment.getId());
//        dto.setSubjectId(assignment.getSubject().getId());
//        dto.setSubjectName(assignment.getSubject().getSubjectName());
//        dto.setUnitNumber(assignment.getUnitNumber());
//        dto.setTitle(assignment.getTitle());
//        dto.setDescription(assignment.getDescription());
//        dto.setDeadline(assignment.getDeadline());
//        dto.setMaxMarks(assignment.getMaxMarks());
//        dto.setGoogleClassroomLink(assignment.getGoogleClassroomLink());
//        dto.setIsActive(assignment.getIsActive());
//        dto.setCreatedAt(assignment.getCreatedAt());
//        dto.setCreatedByName(assignment.getCreatedBy().getUser().getFullName());
//
//        // Get statistics
//        dto.setTotalSubmissions(submissionRepository.countSubmissionsByAssignmentId(assignment.getId()));
//        dto.setGradedSubmissions(submissionRepository.countGradedSubmissionsByAssignmentId(assignment.getId()));
//
//        return dto;
//    }
//
//    private AssignmentSubmissionDTO convertToSubmissionDTO(AssignmentSubmission submission) {
//        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
//        dto.setId(submission.getId());
//        dto.setAssignmentId(submission.getAssignment().getId());
//        dto.setAssignmentTitle(submission.getAssignment().getTitle());
//        dto.setUnitNumber(submission.getAssignment().getUnitNumber());
//        dto.setStudentId(submission.getStudent().getId());
//        dto.setStudentName(submission.getStudent().getUser().getFullName());
//        dto.setRollNumber(submission.getStudent().getRollNumber());
//        dto.setSubmissionFileUrl(submission.getSubmissionFileUrl());
//        dto.setSubmittedAt(submission.getSubmittedAt());
//        dto.setMarksObtained(submission.getMarksObtained());
//        dto.setIsLate(submission.getIsLate());
//        dto.setIsGraded(submission.getIsGraded());
//        dto.setFacultyRemarks(submission.getFacultyRemarks());
//        dto.setGradedAt(submission.getGradedAt());
//        dto.setDeadline(submission.getAssignment().getDeadline());
//
//        if (submission.getGradedBy() != null) {
//            dto.setGradedByName(submission.getGradedBy().getUser().getFullName());
//        }
//
//        return dto;
//    }
//}



//--------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------

package com.cia.management_system.service;

import com.cia.management_system.dto.*;
import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository submissionRepository;
    private final SubjectRepository subjectRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public AssignmentDTO createAssignment(CreateAssignmentRequest request, String username) {
        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Assignment existingAssignment = assignmentRepository
                .findBySubjectIdAndUnitNumber(request.getSubjectId(), request.getUnitNumber());

        if (existingAssignment != null) {
            throw new RuntimeException("Assignment already exists for Unit " + request.getUnitNumber());
        }

        Assignment assignment = new Assignment();
        assignment.setSubject(subject);
        assignment.setUnitNumber(request.getUnitNumber());
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDeadline(request.getDeadline());
        assignment.setMaxMarks(2.0);
        assignment.setGoogleClassroomLink(request.getGoogleClassroomLink());
        assignment.setCreatedBy(faculty);
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setIsActive(true);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return convertToDTO(savedAssignment);
    }

    @Transactional(readOnly = true)
    public List<AssignmentDTO> getAssignmentsBySubject(Long subjectId) {
        List<Assignment> assignments = assignmentRepository.findBySubjectIdOrderByUnitNumberAsc(subjectId);

        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentDTO> getAssignmentsByFaculty(String username) {
        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        List<Assignment> assignments = assignmentRepository.findByFacultyId(faculty.getId());

        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // NEW: Get assignments for student based on their enrollments
    @Transactional(readOnly = true)
    public List<AssignmentDTO> getAssignmentsForStudent(String username) {
        Student student = (Student) studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get all subjects student is enrolled in
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(student.getId());

        // Get all assignments for those subjects
        List<Assignment> allAssignments = enrollments.stream()
                .flatMap(enrollment ->
                        assignmentRepository.findBySubjectIdOrderByUnitNumberAsc(
                                enrollment.getSubject().getId()).stream())
                .collect(Collectors.toList());

        return allAssignments.stream()
                .map(assignment -> convertToDTOForStudent(assignment, student.getId()))
                .collect(Collectors.toList());
    }

    // NEW: Student marks their own submission
    @Transactional
    public AssignmentSubmissionDTO studentMarkSubmission(Long assignmentId, String username, String submissionUrl) {
        Student student = (Student) studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Verify student is enrolled in this subject
        boolean isEnrolled = enrollmentRepository.findByStudentIdAndSubjectId(
                student.getId(), assignment.getSubject().getId()).isPresent();

        if (!isEnrolled) {
            throw new RuntimeException("You are not enrolled in this subject");
        }

        // Check if already submitted
        AssignmentSubmission submission = submissionRepository
                .findByAssignmentIdAndStudentId(assignmentId, student.getId())
                .orElse(new AssignmentSubmission());

        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmissionFileUrl(submissionUrl);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setIsLate(LocalDateTime.now().isAfter(assignment.getDeadline()));
        submission.setIsGraded(false); // Reset grading status if resubmitting

        AssignmentSubmission savedSubmission = submissionRepository.save(submission);

        return convertToSubmissionDTO(savedSubmission);
    }

    @Transactional
    public AssignmentSubmissionDTO markSubmission(Long assignmentId, MarkSubmissionRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        AssignmentSubmission submission = submissionRepository
                .findByAssignmentIdAndStudentId(assignmentId, student.getId())
                .orElse(new AssignmentSubmission());

        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmissionFileUrl(request.getSubmissionFileUrl());
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setIsLate(LocalDateTime.now().isAfter(assignment.getDeadline()));

        AssignmentSubmission savedSubmission = submissionRepository.save(submission);

        return convertToSubmissionDTO(savedSubmission);
    }

    @Transactional
    public AssignmentSubmissionDTO gradeSubmission(Long submissionId, GradeAssignmentRequest request, String username) {
        AssignmentSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        submission.setMarksObtained(request.getMarksObtained());
        submission.setFacultyRemarks(request.getFacultyRemarks());
        submission.setIsGraded(true);
        submission.setGradedAt(LocalDateTime.now());
        submission.setGradedBy(faculty);

        AssignmentSubmission gradedSubmission = submissionRepository.save(submission);

        // TODO: Trigger CIA score recalculation

        return convertToSubmissionDTO(gradedSubmission);
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDTO> getSubmissionsByAssignment(Long assignmentId) {
        List<AssignmentSubmission> submissions = submissionRepository.findByAssignmentId(assignmentId);

        return submissions.stream()
                .map(this::convertToSubmissionDTO)
                .collect(Collectors.toList());
    }

    // NEW: Get pending submissions for faculty (notification)
    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDTO> getPendingSubmissionsForFaculty(String username) {
        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        List<Assignment> assignments = assignmentRepository.findByFacultyId(faculty.getId());

        List<AssignmentSubmission> pendingSubmissions = assignments.stream()
                .flatMap(assignment ->
                        submissionRepository.findByAssignmentId(assignment.getId()).stream())
                .filter(submission -> !submission.getIsGraded())
                .collect(Collectors.toList());

        return pendingSubmissions.stream()
                .map(this::convertToSubmissionDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AssignmentSubmissionDTO> getSubmissionsByStudent(String username) {
        Student student = (Student) studentRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<AssignmentSubmission> submissions = submissionRepository.findByStudentId(student.getId());

        return submissions.stream()
                .map(this::convertToSubmissionDTO)
                .collect(Collectors.toList());
    }

    // Helper methods
    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setSubjectId(assignment.getSubject().getId());
        dto.setSubjectName(assignment.getSubject().getSubjectName());
        dto.setUnitNumber(assignment.getUnitNumber());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDeadline(assignment.getDeadline());
        dto.setMaxMarks(assignment.getMaxMarks());
        dto.setGoogleClassroomLink(assignment.getGoogleClassroomLink());
        dto.setIsActive(assignment.getIsActive());
        dto.setCreatedAt(assignment.getCreatedAt());
        dto.setCreatedByName(assignment.getCreatedBy().getUser().getFullName());

        dto.setTotalSubmissions(submissionRepository.countSubmissionsByAssignmentId(assignment.getId()));
        dto.setGradedSubmissions(submissionRepository.countGradedSubmissionsByAssignmentId(assignment.getId()));

        return dto;
    }

    // NEW: Convert with student submission status
    private AssignmentDTO convertToDTOForStudent(Assignment assignment, Long studentId) {
        AssignmentDTO dto = convertToDTO(assignment);

        // Check if student has submitted
        submissionRepository.findByAssignmentIdAndStudentId(assignment.getId(), studentId)
                .ifPresent(submission -> {
                    dto.setIsSubmitted(true);
                    dto.setSubmissionId(submission.getId());
                    dto.setSubmittedAt(submission.getSubmittedAt());
                    dto.setIsGraded(submission.getIsGraded());
                    dto.setMarksObtained(submission.getMarksObtained());
                });

        return dto;
    }

//    private AssignmentSubmissionDTO convertToSubmissionDTO(AssignmentSubmission submission) {
//        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
//        dto.setId(submission.getId());
//        dto.setAssignmentId(submission.getAssignment().getId());
//        dto.setAssignmentTitle(submission.getAssignment().getTitle());
//        dto.setUnitNumber(submission.getAssignment().getUnitNumber());
//        dto.setStudentId(submission.getStudent().getId());
//        dto.setStudentName(submission.getStudent().getUser().getFullName());
//        dto.setRollNumber(submission.getStudent().getRollNumber());
//        dto.setSubmissionFileUrl(submission.getSubmissionFileUrl());
//        dto.setSubmittedAt(submission.getSubmittedAt());
//        dto.setMarksObtained(submission.getMarksObtained());
//        dto.setIsLate(submission.getIsLate());
//        dto.setIsGraded(submission.getIsGraded());
//        dto.setFacultyRemarks(submission.getFacultyRemarks());
//        dto.setGradedAt(submission.getGradedAt());
//        dto.setDeadline(submission.getAssignment().getDeadline());
//
//        if (submission.getGradedBy() != null) {
//            dto.setGradedByName(submission.getGradedBy().getUser().getFullName());
//        }
//
//        return dto;
//    }


    private AssignmentSubmissionDTO convertToSubmissionDTO(AssignmentSubmission submission) {
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setId(submission.getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setAssignmentTitle(submission.getAssignment().getTitle());

        // ✅ ADD SUBJECT INFORMATION
        dto.setSubjectId(submission.getAssignment().getSubject().getId());
        dto.setSubjectName(submission.getAssignment().getSubject().getSubjectName());
        dto.setSubjectCode(submission.getAssignment().getSubject().getSubjectCode());

        dto.setUnitNumber(submission.getAssignment().getUnitNumber());
        dto.setStudentId(submission.getStudent().getId());
        dto.setStudentName(submission.getStudent().getUser().getFullName());
        dto.setRollNumber(submission.getStudent().getRollNumber());
        dto.setSubmissionFileUrl(submission.getSubmissionFileUrl());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setMarksObtained(submission.getMarksObtained());
        dto.setMaxMarks(submission.getAssignment().getMaxMarks());
        dto.setIsLate(submission.getIsLate());
        dto.setIsGraded(submission.getIsGraded());
        dto.setFacultyRemarks(submission.getFacultyRemarks());
        dto.setGradedAt(submission.getGradedAt());

        if (submission.getGradedBy() != null) {
            dto.setGradedByName(submission.getGradedBy().getUser().getFullName());
        }

        dto.setDeadline(submission.getAssignment().getDeadline());

        return dto;
    }
}
