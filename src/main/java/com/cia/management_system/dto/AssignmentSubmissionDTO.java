package com.cia.management_system.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class AssignmentSubmissionDTO {
//    private Long id;
//    private Long assignmentId;
//    private String assignmentTitle;
//    private Integer unitNumber;
//    private Long studentId;
//    private String studentName;
//    private String rollNumber;
//    private String submissionFileUrl;
//    private LocalDateTime submittedAt;
//    private Double marksObtained;
//    private Boolean isLate;
//    private Boolean isGraded;
//    private String facultyRemarks;
//    private LocalDateTime gradedAt;
//    private String gradedByName;
//    private LocalDateTime deadline;
//}


//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmissionDTO {
    private Long id;
    private Long assignmentId;
    private String assignmentTitle;
    private Long subjectId;          // ✅ MAKE SURE THIS IS HERE
    private String subjectName;      // ✅ AND THIS
    private String subjectCode;      // ✅ AND THIS
    private Integer unitNumber;
    private Long studentId;
    private String studentName;
    private String rollNumber;
    private String submissionFileUrl;
    private LocalDateTime submittedAt;
    private Double marksObtained;
    private Double maxMarks;
    private Boolean isLate;
    private Boolean isGraded;
    private String facultyRemarks;
    private LocalDateTime gradedAt;
    private String gradedByName;
    private LocalDateTime deadline;
}
