package com.cia.management_system.service;



import com.cia.management_system.dto.EnrollmentDTO;
import com.cia.management_system.model.Enrollment;
import com.cia.management_system.model.Faculty;
import com.cia.management_system.model.Student;
import com.cia.management_system.model.Subject;
import com.cia.management_system.repository.EnrollmentRepository;
import com.cia.management_system.repository.FacultyRepository;
import com.cia.management_system.repository.StudentRepository;
import com.cia.management_system.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;

    @Transactional
    public EnrollmentDTO enrollStudent(Long studentId, Long subjectId, Long facultyId, String academicYear) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // Check if already enrolled
        if (enrollmentRepository.findByStudentIdAndSubjectIdAndAcademicYear(
                studentId, subjectId, academicYear).isPresent()) {
            throw new RuntimeException("Student already enrolled in this subject");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setFaculty(faculty);
        enrollment.setAcademicYear(academicYear);
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setIsActive(true);

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return convertToDTO(savedEnrollment);
    }

    @Transactional
    public void bulkEnrollStudents(List<Long> studentIds, Long subjectId, Long facultyId, String academicYear) {
        for (Long studentId : studentIds) {
            try {
                enrollStudent(studentId, subjectId, facultyId, academicYear);
            } catch (RuntimeException e) {
                // Skip if already enrolled
                if (!e.getMessage().contains("already enrolled")) {
                    throw e;
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);
        return enrollments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getEnrollmentsBySubject(Long subjectId) {
        List<Enrollment> enrollments = enrollmentRepository.findBySubjectId(subjectId);
        return enrollments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getEnrollmentsByFaculty(Long facultyId) {
        List<Enrollment> enrollments = enrollmentRepository.findByFacultyId(facultyId);
        return enrollments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setIsActive(false);
        enrollmentRepository.save(enrollment);
    }

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(enrollment.getStudent().getUser().getFullName());
        dto.setRollNumber(enrollment.getStudent().getRollNumber());
        dto.setSubjectId(enrollment.getSubject().getId());
        dto.setSubjectName(enrollment.getSubject().getSubjectName());
        dto.setSubjectCode(enrollment.getSubject().getSubjectCode());
        dto.setFacultyId(enrollment.getFaculty().getId());
        dto.setFacultyName(enrollment.getFaculty().getUser().getFullName());
        dto.setAcademicYear(enrollment.getAcademicYear());
        dto.setEnrolledAt(enrollment.getEnrolledAt());
        dto.setIsActive(enrollment.getIsActive());
        return dto;
    }
}