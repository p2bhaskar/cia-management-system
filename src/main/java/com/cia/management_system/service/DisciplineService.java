package com.cia.management_system.service;



import com.cia.management_system.dto.DisciplineMarksDTO;
import com.cia.management_system.dto.UpdateDisciplineMarksRequest;
import com.cia.management_system.model.DisciplineMarks;
import com.cia.management_system.model.Faculty;
import com.cia.management_system.model.Student;
import com.cia.management_system.model.Subject;
import com.cia.management_system.repository.DisciplineMarksRepository;
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
public class DisciplineService {

    private final DisciplineMarksRepository disciplineRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;

    @Transactional
    public DisciplineMarksDTO updateDisciplineMarks(
            Long subjectId,
            UpdateDisciplineMarksRequest request,
            String username) {

        Faculty faculty = facultyRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        DisciplineMarks discipline = disciplineRepository
                .findByStudentIdAndSubjectId(student.getId(), subjectId)
                .orElse(new DisciplineMarks());

        discipline.setStudent(student);
        discipline.setSubject(subject);
        discipline.setMarksObtained(request.getMarksObtained());
        discipline.setRemarks(request.getRemarks());
        discipline.setLastUpdated(LocalDateTime.now());
        discipline.setUpdatedBy(faculty);

        DisciplineMarks saved = disciplineRepository.save(discipline);

        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<DisciplineMarksDTO> getDisciplineMarksBySubject(Long subjectId) {
        List<DisciplineMarks> disciplineList = disciplineRepository.findBySubjectId(subjectId);

        return disciplineList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DisciplineMarksDTO> getDisciplineMarksByStudent(Long studentId) {
        List<DisciplineMarks> disciplineList = disciplineRepository.findByStudentId(studentId);

        return disciplineList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DisciplineMarksDTO getDisciplineMarks(Long studentId, Long subjectId) {
        return disciplineRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    private DisciplineMarksDTO convertToDTO(DisciplineMarks discipline) {
        DisciplineMarksDTO dto = new DisciplineMarksDTO();
        dto.setId(discipline.getId());
        dto.setStudentId(discipline.getStudent().getId());
        dto.setStudentName(discipline.getStudent().getUser().getFullName());
        dto.setRollNumber(discipline.getStudent().getRollNumber());
        dto.setSubjectId(discipline.getSubject().getId());
        dto.setSubjectName(discipline.getSubject().getSubjectName());
        dto.setMarksObtained(discipline.getMarksObtained());
        dto.setRemarks(discipline.getRemarks());
        dto.setLastUpdated(discipline.getLastUpdated());

        if (discipline.getUpdatedBy() != null) {
            dto.setUpdatedByName(discipline.getUpdatedBy().getUser().getFullName());
        }

        return dto;
    }
}