//package com.cia.management_system.service;
//
//import com.cia.management_system.dto.*;
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CertificateService {
//
//    private final CertificateRepository certificateRepository;
//    private final ActivityMarksRepository activityMarksRepository;
//    private final StudentRepository studentRepository;
//    private final SubjectRepository subjectRepository;
//    private final FacultyRepository facultyRepository;
//
//    // ==================== STUDENT OPERATIONS ====================
//
//    @Transactional
//    public CertificateDTO uploadCertificate(UploadCertificateRequest request, String studentUsername) {
//        Student student = studentRepository.findByUserUsername(studentUsername)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Subject subject = subjectRepository.findById(request.getSubjectId())
//                .orElseThrow(() -> new RuntimeException("Subject not found"));
//
//        // Check limits
//        Long approvedCount = certificateRepository.countApprovedByStudentAndSubjectAndType(
//                student.getId(),
//                subject.getId(),
//                request.getCertificateType()
//        );
//
//        if (approvedCount >= 2) {
//            throw new RuntimeException("Maximum 2 " + request.getCertificateType() + " certificates allowed per subject");
//        }
//
//        Certificate certificate = new Certificate();
//        certificate.setStudent(student);
//        certificate.setSubject(subject);
//        certificate.setCertificateType(request.getCertificateType());
//        certificate.setCertificateTitle(request.getCertificateTitle());
//        certificate.setCertificateUrl(request.getCertificateUrl());
//        certificate.setUploadDate(LocalDateTime.now());
//        certificate.setStatus(Certificate.CertificateStatus.PENDING);
//        certificate.setMarksAwarded(BigDecimal.valueOf(0.0));
//
//        Certificate saved = certificateRepository.save(certificate);
//
//        return convertToDTO(saved);
//    }
//
//    @Transactional(readOnly = true)
//    public List<CertificateDTO> getMyCertificates(String studentUsername) {
//        Student student = studentRepository.findByUserUsername(studentUsername)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        List<Certificate> certificates = certificateRepository.findByStudentId(student.getId());
//        return certificates.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<CertificateDTO> getMyCertificatesBySubject(Long subjectId, String studentUsername) {
//        Student student = studentRepository.findByUserUsername(studentUsername)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        List<Certificate> certificates = certificateRepository
//                .findByStudentIdAndSubjectId(student.getId(), subjectId);
//
//        return certificates.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // ==================== FACULTY OPERATIONS ====================
//
//    @Transactional(readOnly = true)
//    public List<CertificateDTO> getPendingCertificates(Long subjectId) {
//        List<Certificate> certificates = certificateRepository
//                .findBySubjectIdAndStatus(subjectId, Certificate.CertificateStatus.PENDING);
//
//        return certificates.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<CertificateDTO> getAllCertificatesBySubject(Long subjectId) {
//        List<Certificate> certificates = certificateRepository.findBySubjectId(subjectId);
//        return certificates.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public CertificateDTO reviewCertificate(Long certificateId, ReviewCertificateRequest request, String facultyUsername) {
//        Certificate certificate = certificateRepository.findById(certificateId)
//                .orElseThrow(() -> new RuntimeException("Certificate not found"));
//
//        Faculty faculty = facultyRepository.findByUserUsername(facultyUsername)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        // Update certificate status
//        certificate.setStatus(request.getStatus());
//        certificate.setReviewedBy(faculty);
//        certificate.setReviewedAt(LocalDateTime.now());
//        certificate.setRemarks(request.getRemarks());
//
//        // Award marks if approved
//        if (request.getStatus() == Certificate.CertificateStatus.APPROVED) {
//            double marks = certificate.getCertificateType() == Certificate.CertificateType.NPTEL ? 3.0 : 2.0;
//            certificate.setMarksAwarded(BigDecimal.valueOf(marks));
//        } else {
//            certificate.setMarksAwarded(BigDecimal.valueOf(0.0));
//        }
//
//        Certificate saved = certificateRepository.save(certificate);
//
//        // Recalculate activity marks
//        recalculateActivityMarks(certificate.getStudent().getId(), certificate.getSubject().getId(), faculty);
//
//        return convertToDTO(saved);
//    }
//
//    // ==================== ACTIVITY MARKS ====================
//
//    @Transactional
//    public void recalculateActivityMarks(Long studentId, Long subjectId, Faculty updatedBy) {
//        // Get all approved certificates
//        List<Certificate> certificates = certificateRepository
//                .findByStudentIdAndSubjectId(studentId, subjectId)
//                .stream()
//                .filter(c -> c.getStatus() == Certificate.CertificateStatus.APPROVED)
//                .collect(Collectors.toList());
//
//        // Calculate NPTEL marks (max 2 certificates × 3 marks = 6)
//        double nptelMarks = certificates.stream()
//                .filter(c -> c.getCertificateType() == Certificate.CertificateType.NPTEL)
//                .limit(2)
//                .mapToDouble(Certificate::getMarksAwarded)
//                .sum();
//
//        // Calculate SDP marks (max 2 programs × 2 marks = 4)
//        double sdpMarks = certificates.stream()
//                .filter(c -> c.getCertificateType() == Certificate.CertificateType.SDP)
//                .limit(2)
//                .mapToDouble(Certificate::getMarksAwarded)
//                .sum();
//
//        double totalMarks = Math.min(nptelMarks + sdpMarks, 10.0); // Cap at 10
//
//        // Find or create activity marks record
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Subject subject = subjectRepository.findById(subjectId)
//                .orElseThrow(() -> new RuntimeException("Subject not found"));
//
//        ActivityMarks activityMarks = activityMarksRepository
//                .findByStudentIdAndSubjectId(studentId, subjectId)
//                .orElse(new ActivityMarks());
//
//        activityMarks.setStudent(student);
//        activityMarks.setSubject(subject);
//        activityMarks.setNptelMarks(nptelMarks);
//        activityMarks.setSdpMarks(sdpMarks);
//        activityMarks.setTotalMarks(totalMarks);
//        activityMarks.setLastUpdated(LocalDateTime.now());
//        activityMarks.setUpdatedBy(updatedBy);
//
//        activityMarksRepository.save(activityMarks);
//    }
//
//    @Transactional(readOnly = true)
//    public ActivityMarksDTO getActivityMarks(Long studentId, Long subjectId) {
//        ActivityMarks marks = activityMarksRepository
//                .findByStudentIdAndSubjectId(studentId, subjectId)
//                .orElse(null);
//
//        if (marks == null) {
//            // Return empty marks
//            Student student = studentRepository.findById(studentId)
//                    .orElseThrow(() -> new RuntimeException("Student not found"));
//            Subject subject = subjectRepository.findById(subjectId)
//                    .orElseThrow(() -> new RuntimeException("Subject not found"));
//
//            ActivityMarksDTO dto = new ActivityMarksDTO();
//            dto.setStudentId(studentId);
//            dto.setStudentName(student.getUser().getFullName());
//            dto.setRollNumber(student.getRollNumber());
//            dto.setSubjectId(subjectId);
//            dto.setSubjectName(subject.getSubjectName());
//            dto.setSubjectCode(subject.getSubjectCode());
//            dto.setNptelMarks(0.0);
//            dto.setSdpMarks(0.0);
//            dto.setTotalMarks(0.0);
//            return dto;
//        }
//
//        return convertActivityMarksToDTO(marks);
//    }
//
//    @Transactional(readOnly = true)
//    public List<ActivityMarksDTO> getActivityMarksByStudent(Long studentId) {
//        List<ActivityMarks> marksList = activityMarksRepository.findByStudentId(studentId);
//        return marksList.stream()
//                .map(this::convertActivityMarksToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // ==================== CONVERTERS ====================
//
//    private CertificateDTO convertToDTO(Certificate certificate) {
//        CertificateDTO dto = new CertificateDTO();
//        dto.setId(certificate.getId());
//        dto.setStudentId(certificate.getStudent().getId());
//        dto.setStudentName(certificate.getStudent().getUser().getFullName());
//        dto.setRollNumber(certificate.getStudent().getRollNumber());
//        dto.setSubjectId(certificate.getSubject().getId());
//        dto.setSubjectName(certificate.getSubject().getSubjectName());
//        dto.setSubjectCode(certificate.getSubject().getSubjectCode());
//        dto.setCertificateType(certificate.getCertificateType());
//        dto.setCertificateTitle(certificate.getCertificateTitle());
//        dto.setCertificateUrl(certificate.getCertificateUrl());
//        dto.setUploadDate(certificate.getUploadDate());
//        dto.setStatus(certificate.getStatus());
//        dto.setMarksAwarded(certificate.getMarksAwarded());
//
//        if (certificate.getReviewedBy() != null) {
//            dto.setReviewedById(certificate.getReviewedBy().getId());
//            dto.setReviewedByName(certificate.getReviewedBy().getUser().getFullName());
//        }
//
//        dto.setReviewedAt(certificate.getReviewedAt());
//        dto.setRemarks(certificate.getRemarks());
//
//        return dto;
//    }
//
//    private ActivityMarksDTO convertActivityMarksToDTO(ActivityMarks marks) {
//        ActivityMarksDTO dto = new ActivityMarksDTO();
//        dto.setId(marks.getId());
//        dto.setStudentId(marks.getStudent().getId());
//        dto.setStudentName(marks.getStudent().getUser().getFullName());
//        dto.setRollNumber(marks.getStudent().getRollNumber());
//        dto.setSubjectId(marks.getSubject().getId());
//        dto.setSubjectName(marks.getSubject().getSubjectName());
//        dto.setSubjectCode(marks.getSubject().getSubjectCode());
//        dto.setNptelMarks(marks.getNptelMarks());
//        dto.setSdpMarks(marks.getSdpMarks());
//        dto.setTotalMarks(marks.getTotalMarks());
//        dto.setLastUpdated(marks.getLastUpdated());
//
//        if (marks.getUpdatedBy() != null) {
//            dto.setUpdatedByName(marks.getUpdatedBy().getUser().getFullName());
//        }
//
//        return dto;
//    }
//}


package com.cia.management_system.service;

import com.cia.management_system.dto.*;
import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final ActivityMarksRepository activityMarksRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;

    // ==================== STUDENT OPERATIONS ====================

    @Transactional
    public CertificateDTO uploadCertificate(UploadCertificateRequest request, String studentUsername) {

        Student student = studentRepository.findByUserUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Long approvedCount = certificateRepository.countApprovedByStudentAndSubjectAndType(
                student.getId(),
                subject.getId(),
                request.getCertificateType()
        );

        if (approvedCount >= 2) {
            throw new RuntimeException(
                    "Maximum 2 " + request.getCertificateType() + " certificates allowed per subject"
            );
        }

        Certificate certificate = new Certificate();
        certificate.setStudent(student);
        certificate.setSubject(subject);
        certificate.setCertificateType(request.getCertificateType());
        certificate.setCertificateTitle(request.getCertificateTitle());
        certificate.setCertificateUrl(request.getCertificateUrl());
        certificate.setUploadDate(LocalDateTime.now());
        certificate.setStatus(Certificate.CertificateStatus.PENDING);
        certificate.setMarksAwarded(BigDecimal.ZERO);

        Certificate saved = certificateRepository.save(certificate);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CertificateDTO> getMyCertificates(String studentUsername) {

        Student student = studentRepository.findByUserUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return certificateRepository.findByStudentId(student.getId())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CertificateDTO> getMyCertificatesBySubject(Long subjectId, String studentUsername) {

        Student student = studentRepository.findByUserUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return certificateRepository
                .findByStudentIdAndSubjectId(student.getId(), subjectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ==================== FACULTY OPERATIONS ====================

    @Transactional(readOnly = true)
    public List<CertificateDTO> getPendingCertificates(Long subjectId) {

        return certificateRepository
                .findBySubjectIdAndStatus(subjectId, Certificate.CertificateStatus.PENDING)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CertificateDTO> getAllCertificatesBySubject(Long subjectId) {

        return certificateRepository.findBySubjectId(subjectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CertificateDTO reviewCertificate(
            Long certificateId,
            ReviewCertificateRequest request,
            String facultyUsername
    ) {

        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        Faculty faculty = facultyRepository.findByUserUsername(facultyUsername)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        certificate.setStatus(request.getStatus());
        certificate.setReviewedBy(faculty);
        certificate.setReviewedAt(LocalDateTime.now());
        certificate.setRemarks(request.getRemarks());

        if (request.getStatus() == Certificate.CertificateStatus.APPROVED) {
            BigDecimal marks = certificate.getCertificateType() == Certificate.CertificateType.NPTEL
                    ? BigDecimal.valueOf(3)
                    : BigDecimal.valueOf(2);

            certificate.setMarksAwarded(marks);
        } else {
            certificate.setMarksAwarded(BigDecimal.ZERO);
        }

        Certificate saved = certificateRepository.save(certificate);

        recalculateActivityMarks(
                certificate.getStudent().getId(),
                certificate.getSubject().getId(),
                faculty
        );

        return convertToDTO(saved);
    }

    // ==================== ACTIVITY MARKS ====================

    @Transactional
    public void recalculateActivityMarks(Long studentId, Long subjectId, Faculty updatedBy) {

        List<Certificate> approvedCertificates = certificateRepository
                .findByStudentIdAndSubjectId(studentId, subjectId)
                .stream()
                .filter(c -> c.getStatus() == Certificate.CertificateStatus.APPROVED)
                .toList();

        BigDecimal nptelMarks = approvedCertificates.stream()
                .filter(c -> c.getCertificateType() == Certificate.CertificateType.NPTEL)
                .limit(2)
                .map(Certificate::getMarksAwarded)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sdpMarks = approvedCertificates.stream()
                .filter(c -> c.getCertificateType() == Certificate.CertificateType.SDP)
                .limit(2)
                .map(Certificate::getMarksAwarded)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMarks = nptelMarks.add(sdpMarks);
        BigDecimal MAX_TOTAL = BigDecimal.valueOf(10);

        if (totalMarks.compareTo(MAX_TOTAL) > 0) {
            totalMarks = MAX_TOTAL;
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        ActivityMarks activityMarks = activityMarksRepository
                .findByStudentIdAndSubjectId(studentId, subjectId)
                .orElse(new ActivityMarks());

        activityMarks.setStudent(student);
        activityMarks.setSubject(subject);
        activityMarks.setNptelMarks(nptelMarks);
        activityMarks.setSdpMarks(sdpMarks);
        activityMarks.setTotalMarks(totalMarks);
        activityMarks.setUpdatedBy(updatedBy);

        activityMarksRepository.save(activityMarks);
    }

    @Transactional(readOnly = true)
    public ActivityMarksDTO getActivityMarks(Long studentId, Long subjectId) {

        ActivityMarks marks = activityMarksRepository
                .findByStudentIdAndSubjectId(studentId, subjectId)
                .orElse(null);

        if (marks == null) {

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            ActivityMarksDTO dto = new ActivityMarksDTO();
            dto.setStudentId(studentId);
            dto.setStudentName(student.getUser().getFullName());
            dto.setRollNumber(student.getRollNumber());
            dto.setSubjectId(subjectId);
            dto.setSubjectName(subject.getSubjectName());
            dto.setSubjectCode(subject.getSubjectCode());
            dto.setNptelMarks(BigDecimal.ZERO);
            dto.setSdpMarks(BigDecimal.ZERO);
            dto.setTotalMarks(BigDecimal.ZERO);

            return dto;
        }

        return convertActivityMarksToDTO(marks);
    }

    @Transactional(readOnly = true)
    public List<ActivityMarksDTO> getActivityMarksByStudent(Long studentId) {

        return activityMarksRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertActivityMarksToDTO)
                .collect(Collectors.toList());
    }

    // ==================== CONVERTERS ====================

    private CertificateDTO convertToDTO(Certificate certificate) {

        CertificateDTO dto = new CertificateDTO();
        dto.setId(certificate.getId());
        dto.setStudentId(certificate.getStudent().getId());
        dto.setStudentName(certificate.getStudent().getUser().getFullName());
        dto.setRollNumber(certificate.getStudent().getRollNumber());
        dto.setSubjectId(certificate.getSubject().getId());
        dto.setSubjectName(certificate.getSubject().getSubjectName());
        dto.setSubjectCode(certificate.getSubject().getSubjectCode());
        dto.setCertificateType(certificate.getCertificateType());
        dto.setCertificateTitle(certificate.getCertificateTitle());
        dto.setCertificateUrl(certificate.getCertificateUrl());
        dto.setUploadDate(certificate.getUploadDate());
        dto.setStatus(certificate.getStatus());
        dto.setMarksAwarded(certificate.getMarksAwarded());

        if (certificate.getReviewedBy() != null) {
            dto.setReviewedById(certificate.getReviewedBy().getId());
            dto.setReviewedByName(certificate.getReviewedBy().getUser().getFullName());
        }

        dto.setReviewedAt(certificate.getReviewedAt());
        dto.setRemarks(certificate.getRemarks());

        return dto;
    }

    private ActivityMarksDTO convertActivityMarksToDTO(ActivityMarks marks) {

        ActivityMarksDTO dto = new ActivityMarksDTO();
        dto.setId(marks.getId());
        dto.setStudentId(marks.getStudent().getId());
        dto.setStudentName(marks.getStudent().getUser().getFullName());
        dto.setRollNumber(marks.getStudent().getRollNumber());
        dto.setSubjectId(marks.getSubject().getId());
        dto.setSubjectName(marks.getSubject().getSubjectName());
        dto.setSubjectCode(marks.getSubject().getSubjectCode());
        dto.setNptelMarks(marks.getNptelMarks());
        dto.setSdpMarks(marks.getSdpMarks());
        dto.setTotalMarks(marks.getTotalMarks());
        dto.setLastUpdated(marks.getLastUpdated());

        if (marks.getUpdatedBy() != null) {
            dto.setUpdatedByName(marks.getUpdatedBy().getUser().getFullName());
        }

        return dto;
    }
}
