package com.cia.management_system.service;



//import com.cia.management_system.model.Subject;
//import com.cia.management_system.repository.SubjectRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SubjectService {
//
//    private final SubjectRepository subjectRepository;
//
//    @Transactional(readOnly = true)
//    public List<Subject> getAllActiveSubjects() {
//        return subjectRepository.findByIsActiveTrueOrderBySubjectNameAsc();
//    }
//
//    @Transactional(readOnly = true)
//    public Subject getSubjectById(Long id) {
//        return subjectRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
//    }
//
//    @Transactional(readOnly = true)
//    public Subject getSubjectByCode(String subjectCode) {
//        return subjectRepository.findBySubjectCode(subjectCode)
//                .orElseThrow(() -> new RuntimeException("Subject not found with code: " + subjectCode));
//    }
//
//    @Transactional(readOnly = true)
//    public List<Subject> getSubjectsBySemesterAndStream(Integer semester, String stream) {
//        return subjectRepository.findBySemesterAndStreamAndIsActiveTrue(semester, stream);
//    }
//
//    @Transactional(readOnly = true)
//    public List<Subject> getSubjectsByStream(String stream) {
//        return subjectRepository.findByStreamAndIsActiveTrueOrderBySemesterAsc(stream);
//    }
//
//    @Transactional
//    public Subject createSubject(Subject subject) {
//        // Check if subject code already exists
//        if (subjectRepository.existsBySubjectCode(subject.getSubjectCode())) {
//            throw new RuntimeException("Subject with code " + subject.getSubjectCode() + " already exists");
//        }
//
//        return subjectRepository.save(subject);
//    }
//
//    @Transactional
//    public Subject updateSubject(Long id, Subject subjectDetails) {
//        Subject subject = getSubjectById(id);
//
//        subject.setSubjectName(subjectDetails.getSubjectName());
//        subject.setSemester(subjectDetails.getSemester());
//        subject.setStream(subjectDetails.getStream());
//        subject.setCredits(subjectDetails.getCredits());
//        subject.setDepartment(subjectDetails.getDepartment());
//        subject.setDescription(subjectDetails.getDescription());
//        subject.setIsActive(subjectDetails.getIsActive());
//        subject.setAcademicYear(subjectDetails.getAcademicYear());
//
//        return subjectRepository.save(subject);
//    }
//
//    @Transactional
//    public void deleteSubject(Long id) {
//        Subject subject = getSubjectById(id);
//        subject.setIsActive(false); // Soft delete
//        subjectRepository.save(subject);
//    }
//}



import com.cia.management_system.model.Faculty;
import com.cia.management_system.model.FacultySubjectAssignment;
import com.cia.management_system.model.Subject;
import com.cia.management_system.repository.FacultyRepository;
import com.cia.management_system.repository.FacultySubjectAssignmentRepository;
import com.cia.management_system.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final FacultySubjectAssignmentRepository facultySubjectAssignmentRepository;

    @Transactional(readOnly = true)
    public List<Subject> getAllActiveSubjects() {
        return subjectRepository.findByIsActiveTrueOrderBySubjectNameAsc();
    }

    // NEW: Get subjects assigned to a specific faculty
//    @Transactional(readOnly = true)
//    public List<Subject> getSubjectsForFaculty(String username) {
//        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        List<FacultySubjectAssignment> assignments =
//                facultySubjectAssignmentRepository.findByFacultyId(faculty.getId());
//
//        return assignments.stream()
//                .map(FacultySubjectAssignment::getSubject)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjectsForFaculty(String username) {
        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // CHANGE THIS LINE: Use the new repository method
        List<FacultySubjectAssignment> assignments =
                facultySubjectAssignmentRepository.findByFacultyIdWithSubjects(faculty.getId());

        return assignments.stream()
                .map(FacultySubjectAssignment::getSubject)
                .filter(subject -> subject.getIsActive())
                .collect(Collectors.toList());
    }

    // UPDATE THIS METHOD ONLY:
    @Transactional(readOnly = true)
    public List<Subject> getSubjectsForFacultyByAcademicYear(String username, String academicYear) {
        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // For this one, you need to add another method to repository or use a workaround
        // OPTION 1: Add this new method to repository (recommended):
        // @Query("SELECT fsa FROM FacultySubjectAssignment fsa " +
        //        "JOIN FETCH fsa.subject " +
        //        "WHERE fsa.faculty.id = :facultyId " +
        //        "AND fsa.academicYear = :academicYear AND fsa.isActive = true")
        // List<FacultySubjectAssignment> findByFacultyIdAndAcademicYearWithSubjects(
        //        @Param("facultyId") Long facultyId, @Param("academicYear") String academicYear);

        // For now, let's fix the immediate issue. First, fetch assignments
        List<FacultySubjectAssignment> assignments =
                facultySubjectAssignmentRepository.findByFacultyIdAndAcademicYear(faculty.getId(), academicYear);

        // Then manually fetch subjects for each assignment within transaction
        return assignments.stream()
                .map(assignment -> {
                    // Since assignment.getSubject() is lazy, fetch it separately
                    return subjectRepository.findById(assignment.getSubject().getId())
                            .orElseThrow(() -> new RuntimeException("Subject not found"));
                })
                .filter(subject -> subject.getIsActive())
                .collect(Collectors.toList());
    }

    // NEW: Get subjects for faculty in current academic year
//    @Transactional(readOnly = true)
//    public List<Subject> getSubjectsForFacultyByAcademicYear(String username, String academicYear) {
//        Faculty faculty = (Faculty) facultyRepository.findByUserUsername(username)
//                .orElseThrow(() -> new RuntimeException("Faculty not found"));
//
//        List<FacultySubjectAssignment> assignments =
//                facultySubjectAssignmentRepository.findByFacultyIdAndAcademicYear(
//                        faculty.getId(), academicYear);
//
//        return assignments.stream()
//                .map(FacultySubjectAssignment::getSubject)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Subject getSubjectByCode(String subjectCode) {
        return subjectRepository.findBySubjectCode(subjectCode)
                .orElseThrow(() -> new RuntimeException("Subject not found with code: " + subjectCode));
    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjectsBySemesterAndStream(Integer semester, String stream) {
        return subjectRepository.findBySemesterAndStreamAndIsActiveTrue(semester, stream);
    }

    @Transactional(readOnly = true)
    public List<Subject> getSubjectsByStream(String stream) {
        return subjectRepository.findByStreamAndIsActiveTrueOrderBySemesterAsc(stream);
    }

    @Transactional
    public Subject createSubject(Subject subject) {
        if (subjectRepository.existsBySubjectCode(subject.getSubjectCode())) {
            throw new RuntimeException("Subject with code " + subject.getSubjectCode() + " already exists");
        }
        return subjectRepository.save(subject);
    }

    @Transactional
    public Subject updateSubject(Long id, Subject subjectDetails) {
        Subject subject = getSubjectById(id);

        subject.setSubjectName(subjectDetails.getSubjectName());
        subject.setSemester(subjectDetails.getSemester());
        subject.setStream(subjectDetails.getStream());
        subject.setCredits(subjectDetails.getCredits());
        subject.setDepartment(subjectDetails.getDepartment());
        subject.setDescription(subjectDetails.getDescription());
        subject.setIsActive(subjectDetails.getIsActive());
        subject.setAcademicYear(subjectDetails.getAcademicYear());

        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }
}
