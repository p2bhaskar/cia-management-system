package com.cia.management_system.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineMarksDTO {
    private Long id;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private Long subjectId;
    private String subjectName;
    private Double marksObtained;
    private String remarks;
    private LocalDateTime lastUpdated;
    private String updatedByName;
}
