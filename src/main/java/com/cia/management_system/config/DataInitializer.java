//package com.cia.management_system.config;
//
//
//
//import com.cia.management_system.model.*;
//import com.cia.management_system.repository.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final StudentRepository studentRepository;
//    private final FacultyRepository facultyRepository;
//    private final SubjectRepository subjectRepository;
//    private final FacultySubjectAssignmentRepository facultySubjectAssignmentRepository;
//    private final EnrollmentRepository enrollmentRepository;
//    private final AssignmentRepository assignmentRepository;
//    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        // Check if data already exists
//        if (userRepository.count() > 0) {
//            log.info("Database already has data. Skipping initialization.");
//            return;
//        }
//
//        log.info("Starting database initialization with dummy data...");
//
//        // 1. Create Admin User
//        User admin = createAdmin();
//
//        // 2. Create Subjects
//        List<Subject> subjects = createSubjects();
//
//        // 3. Create Faculty Users
//        List<Faculty> faculties = createFaculties();
//
//        // 4. Create Student Users
//        List<Student> students = createStudents();
//
//        // 5. Assign Subjects to Faculty
//        assignSubjectsToFaculty(faculties, subjects, admin);
//
//        // 6. Enroll Students in Subjects
//        enrollStudentsInSubjects(students, subjects, faculties);
//
//        // 7. Create Sample Assignments
//        createSampleAssignments(subjects, faculties);
//
//        // 8. Create Sample Submissions
//        createSampleSubmissions(students);
//
//        log.info("Database initialization completed successfully!");
//        log.info("========================================");
//        log.info("LOGIN CREDENTIALS:");
//        log.info("========================================");
//        log.info("ADMIN:");
//        log.info("  Username: admin");
//        log.info("  Password: admin123");
//        log.info("========================================");
//        log.info("FACULTY (3 accounts):");
//        log.info("  Username: faculty1 | Password: faculty123");
//        log.info("  Username: faculty2 | Password: faculty123");
//        log.info("  Username: faculty3 | Password: faculty123");
//        log.info("========================================");
//        log.info("STUDENTS (10 accounts):");
//        log.info("  Username: student1 | Password: student123");
//        log.info("  Username: student2 | Password: student123");
//        log.info("  ... (student3 to student10 with same password)");
//        log.info("========================================");
//    }
//
//    private User createAdmin() {
//        User admin = new User();
//        admin.setUsername("admin");
//        admin.setPassword(passwordEncoder.encode("admin123"));
//        admin.setEmail("admin@university.edu");
//        admin.setFullName("System Administrator");
//        admin.addRole(Role.ADMIN);
//        admin.setEnabled(true);
//        admin.setAccountNonLocked(true);
//        admin.setCreatedAt(LocalDateTime.now());
//        return userRepository.save(admin);
//    }
//
//    private Faculty createFaculty(String username, String password, String email,
//                                  String fullName, String empId, String department) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setEmail(email);
//        user.setFullName(fullName);
//        user.addRole(Role.FACULTY);
//        user.setEnabled(true);
//        user.setAccountNonLocked(true);
//        user.setCreatedAt(LocalDateTime.now());
//        user = userRepository.save(user);
//
//        Faculty faculty = new Faculty();
//        faculty.setUser(user);
//        faculty.setEmployeeId(empId);
//        faculty.setDepartment(department);
//        return faculty;
//    }
//
//    private Student createStudent(String username, String rollNumber, String fullName,
//                                  int semester, String stream, String section) {
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode("student123"));
//        user.setEmail(username + "@student.university.edu");
//        user.setFullName(fullName);
//        user.addRole(Role.STUDENT);
//        user.setEnabled(true);
//        user.setAccountNonLocked(true);
//        user.setCreatedAt(LocalDateTime.now());
//        user = userRepository.save(user);
//
//        Student student = new Student();
//        student.setUser(user);
//        student.setRollNumber(rollNumber);
//        student.setSemester(semester);
//        student.setStream(Student.Stream.valueOf(stream));
//        student.setSection(section);
//        return student;
//    }
//
//    private List<Subject> createSubjects() {
//        List<Subject> subjects = new ArrayList<>();
//
//        // BCA Subjects
//        subjects.add(createSubject("BCA101", "Programming in C", 1, "BCA", 4,
//                "Introduction to C programming language"));
//        subjects.add(createSubject("BCA102", "Data Structures", 2, "BCA", 4,
//                "Arrays, Linked Lists, Trees, Graphs"));
//        subjects.add(createSubject("BCA103", "Database Management", 3, "BCA", 4,
//                "SQL, Normalization, Transactions"));
//        subjects.add(createSubject("BCA104", "Web Development", 4, "BCA", 3,
//                "HTML, CSS, JavaScript, React"));
//        subjects.add(createSubject("BCA105", "Operating Systems", 5, "BCA", 4,
//                "Process Management, Memory Management"));
//
//        // MCA Subjects
//        subjects.add(createSubject("MCA101", "Advanced Java", 1, "MCA", 4,
//                "Spring Boot, Hibernate, Microservices"));
//        subjects.add(createSubject("MCA102", "Machine Learning", 2, "MCA", 4,
//                "Supervised and Unsupervised Learning"));
//        subjects.add(createSubject("MCA103", "Cloud Computing", 3, "MCA", 3,
//                "AWS, Azure, Docker, Kubernetes"));
//        subjects.add(createSubject("MCA104", "Big Data Analytics", 4, "MCA", 4,
//                "Hadoop, Spark, Data Mining"));
//        subjects.add(createSubject("MCA105", "Cyber Security", 5, "MCA", 3,
//                "Network Security, Cryptography"));
//
//        subjects = subjectRepository.saveAll(subjects);
//        log.info("Created {} subjects", subjects.size());
//        return subjects;
//    }
//
//    private Subject createSubject(String code, String name, int semester, String stream,
//                                  int credits, String description) {
//        Subject subject = new Subject();
//        subject.setSubjectCode(code);
//        subject.setSubjectName(name);
//        subject.setSemester(semester);
//        subject.setStream(stream);
//        subject.setCredits(credits);
//        subject.setDepartment("Computer Science & Engineering");
//        subject.setDescription(description);
//        subject.setIsActive(true);
//        subject.setAcademicYear("2024-2025");
//        return subject;
//    }
//
//    private List<Faculty> createFaculties() {
//        List<Faculty> faculties = new ArrayList<>();
//
//        // Faculty 1 - BCA Faculty
//        faculties.add(createFaculty("faculty1", "faculty123", "faculty1@university.edu",
//                "Dr. Rajesh Kumar", "EMP001", "BCA"));
//
//        // Faculty 2 - BCA Faculty
//        faculties.add(createFaculty("faculty2", "faculty123", "faculty2@university.edu",
//                "Prof. Priya Sharma", "EMP002", "BCA"));
//
//        // Faculty 3 - MCA Faculty
//        faculties.add(createFaculty("faculty3", "faculty123", "faculty3@university.edu",
//                "Dr. Amit Singh", "EMP003", "MCA"));
//
//        faculties = facultyRepository.saveAll(faculties);
//        log.info("Created {} faculty members", faculties.size());
//        return faculties;
//    }
//
////    private Faculty createFaculty(String username, String password, String email,
////                                  String fullName, String empId, String department) {
////        User user = new User();
////        user.setUsername(username);
////        user.setPassword(passwordEncoder.encode(password));
////        user.setEmail(email);
////        user.setFullName(fullName);
////        user.setRole(Role.FACULTY);
////        user.setIsActive(true);
////        user.setCreatedAt(LocalDateTime.now());
////        user = userRepository.save(user);
////
////        Faculty faculty = new Faculty();
////        faculty.setUser(user);
////        faculty.setEmployeeId(empId);
////        faculty.setDepartment(department);
////        return faculty;
////    }
//
//    private List<Student> createStudents() {
//        List<Student> students = new ArrayList<>();
//
//        // BCA Students (Semester 1, 2, 3)
//        students.add(createStudent("student1", "240205131001", "Rahul Verma", 1, "BCA", "A"));
//        students.add(createStudent("student2", "240205131002", "Sneha Patel", 1, "BCA", "A"));
//        students.add(createStudent("student3", "240205131003", "Arjun Yadav", 2, "BCA", "A"));
//        students.add(createStudent("student4", "240205131004", "Ananya Reddy", 2, "BCA", "B"));
//        students.add(createStudent("student5", "240205131005", "Vikram Singh", 3, "BCA", "A"));
//
//        // MCA Students (Semester 1, 2)
//        students.add(createStudent("student6", "240305131001", "Neha Gupta", 1, "MCA", "A"));
//        students.add(createStudent("student7", "240305131002", "Karan Mehta", 1, "MCA", "A"));
//        students.add(createStudent("student8", "240305131003", "Pooja Iyer", 2, "MCA", "A"));
//        students.add(createStudent("student9", "240305131004", "Rohit Joshi", 2, "MCA", "A"));
//        students.add(createStudent("student10", "240305131005", "Divya Nair", 3, "MCA", "A"));
//
//        students = studentRepository.saveAll(students);
//        log.info("Created {} students", students.size());
//        return students;
//    }
//
////    private Student createStudent(String username, String rollNumber, String fullName,
////                                  int semester, String stream, String section) {
////        User user = new User();
////        user.setUsername(username);
////        user.setPassword(passwordEncoder.encode("student123"));
////        user.setEmail(username + "@student.university.edu");
////        user.setFullName(fullName);
////        user.setRole(Role.STUDENT);
////        user.setIsActive(true);
////        user.setCreatedAt(LocalDateTime.now());
////        user = userRepository.save(user);
////
////        Student student = new Student();
////        student.setUser(user);
////        student.setRollNumber(rollNumber);
////        student.setSemester(semester);
////        student.setStream(Student.Stream.valueOf(stream));
////        student.setSection(section);
////        return student;
////    }
//
//    private void assignSubjectsToFaculty(List<Faculty> faculties, List<Subject> subjects, User admin) {
//        List<FacultySubjectAssignment> assignments = new ArrayList<>();
//
//        // Faculty 1: BCA Subjects (Sem 1, 2)
//        assignments.add(createFacultySubjectAssignment(faculties.get(0), subjects.get(0), admin)); // BCA101
//        assignments.add(createFacultySubjectAssignment(faculties.get(0), subjects.get(1), admin)); // BCA102
//
//        // Faculty 2: BCA Subjects (Sem 3, 4)
//        assignments.add(createFacultySubjectAssignment(faculties.get(1), subjects.get(2), admin)); // BCA103
//        assignments.add(createFacultySubjectAssignment(faculties.get(1), subjects.get(3), admin)); // BCA104
//
//        // Faculty 3: MCA Subjects (Sem 1, 2, 3)
//        assignments.add(createFacultySubjectAssignment(faculties.get(2), subjects.get(5), admin)); // MCA101
//        assignments.add(createFacultySubjectAssignment(faculties.get(2), subjects.get(6), admin)); // MCA102
//        assignments.add(createFacultySubjectAssignment(faculties.get(2), subjects.get(7), admin)); // MCA103
//
//        facultySubjectAssignmentRepository.saveAll(assignments);
//        log.info("Assigned {} subjects to faculty", assignments.size());
//    }
//
//    private FacultySubjectAssignment createFacultySubjectAssignment(Faculty faculty, Subject subject, User admin) {
//        FacultySubjectAssignment assignment = new FacultySubjectAssignment();
//        assignment.setFaculty(faculty);
//        assignment.setSubject(subject);
//        assignment.setAcademicYear("2024-2025");
//        assignment.setSection(null); // All sections
//        assignment.setAssignedBy(admin);
//        assignment.setAssignedAt(LocalDateTime.now());
//        assignment.setIsActive(true);
//        return assignment;
//    }
//
//    private void enrollStudentsInSubjects(List<Student> students, List<Subject> subjects, List<Faculty> faculties) {
//        List<Enrollment> enrollments = new ArrayList<>();
//
//        // BCA Sem 1 students in BCA101
//        enrollments.add(createEnrollment(students.get(0), subjects.get(0), faculties.get(0)));
//        enrollments.add(createEnrollment(students.get(1), subjects.get(0), faculties.get(0)));
//
//        // BCA Sem 2 students in BCA102
//        enrollments.add(createEnrollment(students.get(2), subjects.get(1), faculties.get(0)));
//        enrollments.add(createEnrollment(students.get(3), subjects.get(1), faculties.get(0)));
//
//        // BCA Sem 3 students in BCA103
//        enrollments.add(createEnrollment(students.get(4), subjects.get(2), faculties.get(1)));
//
//        // MCA Sem 1 students in MCA101
//        enrollments.add(createEnrollment(students.get(5), subjects.get(5), faculties.get(2)));
//        enrollments.add(createEnrollment(students.get(6), subjects.get(5), faculties.get(2)));
//
//        // MCA Sem 2 students in MCA102
//        enrollments.add(createEnrollment(students.get(7), subjects.get(6), faculties.get(2)));
//        enrollments.add(createEnrollment(students.get(8), subjects.get(6), faculties.get(2)));
//
//        enrollmentRepository.saveAll(enrollments);
//        log.info("Created {} enrollments", enrollments.size());
//    }
//
//    private Enrollment createEnrollment(Student student, Subject subject, Faculty faculty) {
//        Enrollment enrollment = new Enrollment();
//        enrollment.setStudent(student);
//        enrollment.setSubject(subject);
//        enrollment.setFaculty(faculty);
//        enrollment.setAcademicYear("2024-2025");
//        enrollment.setEnrolledAt(LocalDateTime.now());
//        enrollment.setIsActive(true);
//        return enrollment;
//    }
//
//    private void createSampleAssignments(List<Subject> subjects, List<Faculty> faculties) {
//        List<Assignment> assignments = new ArrayList<>();
//
//        // Faculty 1: BCA101 - 2 assignments
//        assignments.add(createAssignment(subjects.get(0), faculties.get(0), 1,
//                "Introduction to C - Variables and Data Types",
//                "Complete exercises on variable declarations and data types"));
//        assignments.add(createAssignment(subjects.get(0), faculties.get(0), 2,
//                "Control Structures in C",
//                "Implement programs using if-else and loops"));
//
//        // Faculty 1: BCA102 - 1 assignment
//        assignments.add(createAssignment(subjects.get(1), faculties.get(0), 1,
//                "Array Operations",
//                "Implement array searching and sorting algorithms"));
//
//        // Faculty 2: BCA103 - 2 assignments
//        assignments.add(createAssignment(subjects.get(2), faculties.get(1), 1,
//                "SQL Queries - Basic SELECT",
//                "Write queries for data retrieval"));
//        assignments.add(createAssignment(subjects.get(2), faculties.get(1), 2,
//                "Database Normalization",
//                "Normalize given database schema to 3NF"));
//
//        // Faculty 3: MCA101 - 2 assignments
//        assignments.add(createAssignment(subjects.get(5), faculties.get(2), 1,
//                "Spring Boot REST API",
//                "Create a CRUD REST API using Spring Boot"));
//        assignments.add(createAssignment(subjects.get(5), faculties.get(2), 2,
//                "JPA and Hibernate",
//                "Implement entity relationships using JPA"));
//
//        assignmentRepository.saveAll(assignments);
//        log.info("Created {} assignments", assignments.size());
//    }
//
//    private Assignment createAssignment(Subject subject, Faculty faculty, int unitNumber,
//                                        String title, String description) {
//        Assignment assignment = new Assignment();
//        assignment.setSubject(subject);
//        assignment.setCreatedBy(faculty);
//        assignment.setUnitNumber(unitNumber);
//        assignment.setTitle(title);
//        assignment.setDescription(description);
//        assignment.setDeadline(LocalDateTime.now().plusDays(14)); // 2 weeks from now
//        assignment.setMaxMarks(2.0);
//        assignment.setGoogleClassroomLink("https://classroom.google.com/c/sample" + unitNumber);
//        assignment.setCreatedAt(LocalDateTime.now());
//        assignment.setIsActive(true);
//        return assignment;
//    }
//
//    private void createSampleSubmissions(List<Student> students) {
//        List<Assignment> assignments = assignmentRepository.findAll();
//        List<AssignmentSubmission> submissions = new ArrayList<>();
//
//        // Create some sample submissions
//        if (assignments.size() > 0) {
//            Assignment assignment1 = assignments.get(0);
//
//            // Student 1: Submitted and graded
//            submissions.add(createSubmission(assignment1, students.get(0), 1.5, true, false));
//
//            // Student 2: Submitted but not graded
//            submissions.add(createSubmission(assignment1, students.get(1), null, false, false));
//        }
//
//        if (assignments.size() > 1) {
//            Assignment assignment2 = assignments.get(1);
//
//            // Student 1: Submitted and graded
//            submissions.add(createSubmission(assignment2, students.get(0), 2.0, true, false));
//        }
//
//        if (assignments.size() > 5) {
//            Assignment assignment6 = assignments.get(5);
//
//            // MCA Student 6: Submitted and graded
//            submissions.add(createSubmission(assignment6, students.get(5), 1.8, true, false));
//
//            // MCA Student 7: Submitted but not graded
//            submissions.add(createSubmission(assignment6, students.get(6), null, false, false));
//        }
//
//        assignmentSubmissionRepository.saveAll(submissions);
//        log.info("Created {} submissions", submissions.size());
//    }
//
//    private AssignmentSubmission createSubmission(Assignment assignment, Student student,
//                                                  Double marks, boolean isGraded, boolean isLate) {
//        AssignmentSubmission submission = new AssignmentSubmission();
//        submission.setAssignment(assignment);
//        submission.setStudent(student);
//        submission.setSubmissionFileUrl("https://classroom.google.com/submission/sample");
//        submission.setSubmittedAt(LocalDateTime.now().minusDays(5));
//        submission.setMarksObtained(marks);
//        submission.setIsGraded(isGraded);
//        submission.setIsLate(isLate);
//
//        if (isGraded) {
//            submission.setGradedAt(LocalDateTime.now().minusDays(2));
//            submission.setGradedBy(assignment.getCreatedBy());
//            submission.setFacultyRemarks("Good work! Keep it up.");
//        }
//
//        return submission;
//    }
//}



package com.cia.management_system.config;

import com.cia.management_system.model.*;
import com.cia.management_system.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final SubjectRepository subjectRepository;
    private final FacultySubjectAssignmentRepository facultySubjectAssignmentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (userRepository.count() > 0) {
            log.info("Database already has data. Skipping initialization.");
            return;
        }

        log.info("Starting database initialization with dummy data...");

        // 1. Create Admin User
        User admin = createAdmin();

        // 2. Create Subjects
        List<Subject> subjects = createSubjects();

        // 3. Create Faculty Users
        List<Faculty> faculties = createFaculties();

        // 4. Create Student Users
        List<Student> students = createStudents();

        // 5. Assign Subjects to Faculty
        assignSubjectsToFaculty(faculties, subjects, admin);

        // 6. Enroll Students in Subjects
        enrollStudentsInSubjects(students, subjects, faculties);

        // 7. Create Sample Assignments
        createSampleAssignments(subjects, faculties);

        // 8. Create Sample Submissions
        createSampleSubmissions(students);

        log.info("Database initialization completed successfully!");
        log.info("========================================");
        log.info("LOGIN CREDENTIALS:");
        log.info("========================================");
        log.info("ADMIN:");
        log.info("  Username: admin");
        log.info("  Password: admin123");
        log.info("========================================");
        log.info("FACULTY (3 accounts):");
        log.info("  Username: faculty1 | Password: faculty123");
        log.info("  Username: faculty2 | Password: faculty123");
        log.info("  Username: faculty3 | Password: faculty123");
        log.info("========================================");
        log.info("STUDENTS (10 accounts):");
        log.info("  Username: student1 | Password: student123");
        log.info("  Username: student2 | Password: student123");
        log.info("  ... (student3 to student10 with same password)");
        log.info("========================================");
    }

    private User createAdmin() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@university.edu");
        admin.setFullName("System Administrator");
        admin.addRole(Role.ADMIN);
        admin.setEnabled(true);
        admin.setAccountNonLocked(true);
        admin.setCreatedAt(LocalDateTime.now());
        return userRepository.save(admin);
    }

    private Faculty createFaculty(String username, String password, String email,
                                  String fullName, String empId, String department,
                                  String phoneNumber, String designation, String qualification) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.addRole(Role.FACULTY);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        Faculty faculty = new Faculty();
        faculty.setUser(user);
        faculty.setEmployeeId(empId);
        faculty.setDepartment(department);
        faculty.setPhoneNumber(phoneNumber);
        faculty.setDesignation(designation);
        faculty.setQualification(qualification);
        faculty.setSpecialization("Computer Science");
        faculty.setYearsOfExperience(10);
        faculty.setOfficeRoom("Room 101");
        faculty.setOfficeHours("Mon-Fri 10:00 AM - 12:00 PM");
        return faculty;
    }

    private Student createStudent(String username, String rollNumber, String fullName,
                                  int semester, String stream, String section,
                                  String phoneNumber, String address) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("student123"));
        user.setEmail(username + "@student.university.edu");
        user.setFullName(fullName);
        user.addRole(Role.STUDENT);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        student.setRollNumber(rollNumber);
        student.setPhoneNumber(phoneNumber);
        student.setSemester(semester);
        student.setSection(section);
        student.setStream(Student.Stream.valueOf(stream));
        student.setAddress(address);

        // Set admission year and academic year
        if (rollNumber != null && rollNumber.length() == 12) {
            String yearPrefix = rollNumber.substring(0, 2);
            int admissionYear = 2000 + Integer.parseInt(yearPrefix);
            student.setAdmissionYear(admissionYear);
            student.setAcademicYear(admissionYear + "-" + (admissionYear + 1));
        }

        student.setCurrentCgpa(8.5);
        return student;
    }

    private List<Subject> createSubjects() {
        List<Subject> subjects = new ArrayList<>();

        // BCA Subjects
        subjects.add(createSubject("BCA101", "Programming in C", 1, "BCA", 4,
                "Introduction to C programming language"));
        subjects.add(createSubject("BCA102", "Data Structures", 2, "BCA", 4,
                "Arrays, Linked Lists, Trees, Graphs"));
        subjects.add(createSubject("BCA103", "Database Management", 3, "BCA", 4,
                "SQL, Normalization, Transactions"));
        subjects.add(createSubject("BCA104", "Web Development", 4, "BCA", 3,
                "HTML, CSS, JavaScript, React"));
        subjects.add(createSubject("BCA105", "Operating Systems", 5, "BCA", 4,
                "Process Management, Memory Management"));

        // MCA Subjects
        subjects.add(createSubject("MCA101", "Advanced Java", 1, "MCA", 4,
                "Spring Boot, Hibernate, Microservices"));
        subjects.add(createSubject("MCA102", "Machine Learning", 2, "MCA", 4,
                "Supervised and Unsupervised Learning"));
        subjects.add(createSubject("MCA103", "Cloud Computing", 3, "MCA", 3,
                "AWS, Azure, Docker, Kubernetes"));
        subjects.add(createSubject("MCA104", "Big Data Analytics", 4, "MCA", 4,
                "Hadoop, Spark, Data Mining"));
        subjects.add(createSubject("MCA105", "Cyber Security", 5, "MCA", 3,
                "Network Security, Cryptography"));

        subjects = subjectRepository.saveAll(subjects);
        log.info("Created {} subjects", subjects.size());
        return subjects;
    }

    private Subject createSubject(String code, String name, int semester, String stream,
                                  int credits, String description) {
        Subject subject = new Subject();
        subject.setSubjectCode(code);
        subject.setSubjectName(name);
        subject.setSemester(semester);
        subject.setStream(stream);
        subject.setCredits(credits);
        subject.setDepartment("Computer Science & Engineering");
        subject.setDescription(description);
        subject.setIsActive(true);
        subject.setAcademicYear("2024-2025");
        return subject;
    }

    private List<Faculty> createFaculties() {
        List<Faculty> faculties = new ArrayList<>();

        // Faculty 1 - BCA Faculty
        faculties.add(createFaculty("faculty1", "faculty123", "faculty1@university.edu",
                "Dr. Rajesh Kumar", "EMP001", "BCA",
                "9876543210", "Professor", "Ph.D. in Computer Science"));

        // Faculty 2 - BCA Faculty
        faculties.add(createFaculty("faculty2", "faculty123", "faculty2@university.edu",
                "Prof. Priya Sharma", "EMP002", "BCA",
                "9876543211", "Associate Professor", "M.Tech in Computer Science"));

        // Faculty 3 - MCA Faculty
        faculties.add(createFaculty("faculty3", "faculty123", "faculty3@university.edu",
                "Dr. Amit Singh", "EMP003", "MCA",
                "9876543212", "Professor", "Ph.D. in Information Technology"));

        faculties = facultyRepository.saveAll(faculties);
        log.info("Created {} faculty members", faculties.size());
        return faculties;
    }

    private List<Student> createStudents() {
        List<Student> students = new ArrayList<>();

        // BCA Students (Semester 1, 2, 3)
        students.add(createStudent("student1", "240205131001", "Rahul Verma", 1, "BCA", "A",
                "9876543201", "123 Main Street, Delhi"));
        students.add(createStudent("student2", "240205131002", "Sneha Patel", 1, "BCA", "A",
                "9876543202", "456 Park Avenue, Mumbai"));
        students.add(createStudent("student3", "240205131003", "Arjun Yadav", 2, "BCA", "A",
                "9876543203", "789 Lake Road, Bangalore"));
        students.add(createStudent("student4", "240205131004", "Ananya Reddy", 2, "BCA", "B",
                "9876543204", "321 Garden Street, Hyderabad"));
        students.add(createStudent("student5", "240205131005", "Vikram Singh", 3, "BCA", "A",
                "9876543205", "654 Hill View, Chennai"));

        // MCA Students (Semester 1, 2, 3)
        students.add(createStudent("student6", "240305131001", "Neha Gupta", 1, "MCA", "A",
                "9876543206", "987 Ocean Drive, Kolkata"));
        students.add(createStudent("student7", "240305131002", "Karan Mehta", 1, "MCA", "A",
                "9876543207", "135 River Side, Pune"));
        students.add(createStudent("student8", "240305131003", "Pooja Iyer", 2, "MCA", "A",
                "9876543208", "246 Mountain View, Jaipur"));
        students.add(createStudent("student9", "240305131004", "Rohit Joshi", 2, "MCA", "A",
                "9876543209", "369 Sunset Boulevard, Ahmedabad"));
        students.add(createStudent("student10", "240305131005", "Divya Nair", 3, "MCA", "A",
                "9876543210", "753 Sunrise Avenue, Lucknow"));

        students = studentRepository.saveAll(students);
        log.info("Created {} students", students.size());
        return students;
    }

    private void assignSubjectsToFaculty(List<Faculty> faculties, List<Subject> subjects, User admin) {
        List<FacultySubjectAssignment> assignments = new ArrayList<>();

        // Faculty 1: BCA Subjects (Sem 1, 2)
        assignments.add(createFacultySubjectAssignment(faculties.get(0), subjects.get(0), admin)); // BCA101
        assignments.add(createFacultySubjectAssignment(faculties.get(0), subjects.get(1), admin)); // BCA102

        // Faculty 2: BCA Subjects (Sem 3, 4)
        assignments.add(createFacultySubjectAssignment(faculties.get(1), subjects.get(2), admin)); // BCA103
        assignments.add(createFacultySubjectAssignment(faculties.get(1), subjects.get(3), admin)); // BCA104

        // Faculty 3: MCA Subjects (Sem 1, 2, 3)
        assignments.add(createFacultySubjectAssignment(faculties.get(2), subjects.get(5), admin)); // MCA101
        assignments.add(createFacultySubjectAssignment(faculties.get(2), subjects.get(6), admin)); // MCA102
        assignments.add(createFacultySubjectAssignment(faculties.get(2), subjects.get(7), admin)); // MCA103

        facultySubjectAssignmentRepository.saveAll(assignments);
        log.info("Assigned {} subjects to faculty", assignments.size());
    }

    private FacultySubjectAssignment createFacultySubjectAssignment(Faculty faculty, Subject subject, User admin) {
        FacultySubjectAssignment assignment = new FacultySubjectAssignment();
        assignment.setFaculty(faculty);
        assignment.setSubject(subject);
        assignment.setAcademicYear("2024-2025");
        assignment.setSection(null); // All sections
        assignment.setAssignedBy(admin);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setIsActive(true);
        return assignment;
    }

    private void enrollStudentsInSubjects(List<Student> students, List<Subject> subjects, List<Faculty> faculties) {
        List<Enrollment> enrollments = new ArrayList<>();

        // BCA Sem 1 students in BCA101
        enrollments.add(createEnrollment(students.get(0), subjects.get(0), faculties.get(0)));
        enrollments.add(createEnrollment(students.get(1), subjects.get(0), faculties.get(0)));

        // BCA Sem 2 students in BCA102
        enrollments.add(createEnrollment(students.get(2), subjects.get(1), faculties.get(0)));
        enrollments.add(createEnrollment(students.get(3), subjects.get(1), faculties.get(0)));

        // BCA Sem 3 students in BCA103
        enrollments.add(createEnrollment(students.get(4), subjects.get(2), faculties.get(1)));

        // MCA Sem 1 students in MCA101
        enrollments.add(createEnrollment(students.get(5), subjects.get(5), faculties.get(2)));
        enrollments.add(createEnrollment(students.get(6), subjects.get(5), faculties.get(2)));

        // MCA Sem 2 students in MCA102
        enrollments.add(createEnrollment(students.get(7), subjects.get(6), faculties.get(2)));
        enrollments.add(createEnrollment(students.get(8), subjects.get(6), faculties.get(2)));

        enrollmentRepository.saveAll(enrollments);
        log.info("Created {} enrollments", enrollments.size());
    }

    private Enrollment createEnrollment(Student student, Subject subject, Faculty faculty) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setFaculty(faculty);
        enrollment.setAcademicYear("2024-2025");
        enrollment.setEnrolledAt(LocalDateTime.now());
        enrollment.setIsActive(true);
        return enrollment;
    }

    private void createSampleAssignments(List<Subject> subjects, List<Faculty> faculties) {
        List<Assignment> assignments = new ArrayList<>();

        // Faculty 1: BCA101 - 2 assignments
        assignments.add(createAssignment(subjects.get(0), faculties.get(0), 1,
                "Introduction to C - Variables and Data Types",
                "Complete exercises on variable declarations and data types"));
        assignments.add(createAssignment(subjects.get(0), faculties.get(0), 2,
                "Control Structures in C",
                "Implement programs using if-else and loops"));

        // Faculty 1: BCA102 - 1 assignment
        assignments.add(createAssignment(subjects.get(1), faculties.get(0), 1,
                "Array Operations",
                "Implement array searching and sorting algorithms"));

        // Faculty 2: BCA103 - 2 assignments
        assignments.add(createAssignment(subjects.get(2), faculties.get(1), 1,
                "SQL Queries - Basic SELECT",
                "Write queries for data retrieval"));
        assignments.add(createAssignment(subjects.get(2), faculties.get(1), 2,
                "Database Normalization",
                "Normalize given database schema to 3NF"));

        // Faculty 3: MCA101 - 2 assignments
        assignments.add(createAssignment(subjects.get(5), faculties.get(2), 1,
                "Spring Boot REST API",
                "Create a CRUD REST API using Spring Boot"));
        assignments.add(createAssignment(subjects.get(5), faculties.get(2), 2,
                "JPA and Hibernate",
                "Implement entity relationships using JPA"));

        assignmentRepository.saveAll(assignments);
        log.info("Created {} assignments", assignments.size());
    }

    private Assignment createAssignment(Subject subject, Faculty faculty, int unitNumber,
                                        String title, String description) {
        Assignment assignment = new Assignment();
        assignment.setSubject(subject);
        assignment.setCreatedBy(faculty);
        assignment.setUnitNumber(unitNumber);
        assignment.setTitle(title);
        assignment.setDescription(description);
        assignment.setDeadline(LocalDateTime.now().plusDays(14)); // 2 weeks from now
        assignment.setMaxMarks(2.0);
        assignment.setGoogleClassroomLink("https://classroom.google.com/c/sample" + unitNumber);
        assignment.setCreatedAt(LocalDateTime.now());
        assignment.setIsActive(true);
        return assignment;
    }

    private void createSampleSubmissions(List<Student> students) {
        List<Assignment> assignments = assignmentRepository.findAll();
        List<AssignmentSubmission> submissions = new ArrayList<>();

        // Create some sample submissions
        if (assignments.size() > 0) {
            Assignment assignment1 = assignments.get(0);

            // Student 1: Submitted and graded
            submissions.add(createSubmission(assignment1, students.get(0), 1.5, true, false));

            // Student 2: Submitted but not graded
            submissions.add(createSubmission(assignment1, students.get(1), null, false, false));
        }

        if (assignments.size() > 1) {
            Assignment assignment2 = assignments.get(1);

            // Student 1: Submitted and graded
            submissions.add(createSubmission(assignment2, students.get(0), 2.0, true, false));
        }

        if (assignments.size() > 5) {
            Assignment assignment6 = assignments.get(5);

            // MCA Student 6: Submitted and graded
            submissions.add(createSubmission(assignment6, students.get(5), 1.8, true, false));

            // MCA Student 7: Submitted but not graded
            submissions.add(createSubmission(assignment6, students.get(6), null, false, false));
        }

        assignmentSubmissionRepository.saveAll(submissions);
        log.info("Created {} submissions", submissions.size());
    }

    private AssignmentSubmission createSubmission(Assignment assignment, Student student,
                                                  Double marks, boolean isGraded, boolean isLate) {
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmissionFileUrl("https://classroom.google.com/submission/sample");
        submission.setSubmittedAt(LocalDateTime.now().minusDays(5));
        submission.setMarksObtained(marks);
        submission.setIsGraded(isGraded);
        submission.setIsLate(isLate);

        if (isGraded) {
            submission.setGradedAt(LocalDateTime.now().minusDays(2));
            submission.setGradedBy(assignment.getCreatedBy());
            submission.setFacultyRemarks("Good work! Keep it up.");
        }

        return submission;
    }
}