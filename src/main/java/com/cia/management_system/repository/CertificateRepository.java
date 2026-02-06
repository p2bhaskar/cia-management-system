package com.cia.management_system.repository;

import com.cia.management_system.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByStudentId(Long studentId);

    List<Certificate> findBySubjectId(Long subjectId);

    @Query("SELECT c FROM Certificate c WHERE c.student.id = :studentId AND c.subject.id = :subjectId")
    List<Certificate> findByStudentIdAndSubjectId(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId
    );

    List<Certificate> findByStatus(Certificate.CertificateStatus status);

    @Query("SELECT c FROM Certificate c WHERE c.subject.id = :subjectId AND c.status = :status")
    List<Certificate> findBySubjectIdAndStatus(
            @Param("subjectId") Long subjectId,
            @Param("status") Certificate.CertificateStatus status
    );

    @Query("SELECT COUNT(c) FROM Certificate c WHERE c.student.id = :studentId AND c.subject.id = :subjectId AND c.certificateType = :type AND c.status = 'APPROVED'")
    Long countApprovedByStudentAndSubjectAndType(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("type") Certificate.CertificateType type
    );
}
