-- Insert sample subjects
INSERT INTO subjects (subject_code, subject_name, semester, stream, credits, department, description, is_active, academic_year) VALUES
('BCA101', 'Programming in C', 1, 'BCA', 4, 'Computer Science & Engineering', 'Introduction to C programming language', true, '2024-2025'),
('BCA102', 'Data Structures', 2, 'BCA', 4, 'Computer Science & Engineering', 'Arrays, Linked Lists, Trees, Graphs', true, '2024-2025'),
('BCA103', 'Database Management', 3, 'BCA', 4, 'Computer Science & Engineering', 'SQL, Normalization, Transactions', true, '2024-2025'),
('BCA104', 'Web Development', 4, 'BCA', 3, 'Computer Science & Engineering', 'HTML, CSS, JavaScript, React', true, '2024-2025'),
('BCA105', 'Operating Systems', 5, 'BCA', 4, 'Computer Science & Engineering', 'Process Management, Memory Management', true, '2024-2025'),
('MCA101', 'Advanced Java', 1, 'MCA', 4, 'Computer Science & Engineering', 'Spring Boot, Hibernate, Microservices', true, '2024-2025'),
('MCA102', 'Machine Learning', 2, 'MCA', 4, 'Computer Science & Engineering', 'Supervised and Unsupervised Learning', true, '2024-2025'),
('MCA103', 'Cloud Computing', 3, 'MCA', 3, 'Computer Science & Engineering', 'AWS, Azure, Docker, Kubernetes', true, '2024-2025');
```

---

## Testing the APIs with Postman

### 1. Get All Subjects
```
GET http://localhost:8080/api/subjects
Headers: Authorization: Bearer <jwt_token>
```

### 2. Get Subject by ID
```
GET http://localhost:8080/api/subjects/1
Headers: Authorization: Bearer <jwt_token>
```

### 3. Get Subjects by Semester and Stream
```
GET http://localhost:8080/api/subjects/semester/1/stream/BCA
Headers: Authorization: Bearer <jwt_token>
```

### 4. Create Subject (Admin only)
```
POST http://localhost:8080/api/subjects
Headers:
  Authorization: Bearer <admin_jwt_token>
  Content-Type: application/json
Body:
{
  "subjectCode": "BCA106",
  "subjectName": "Software Engineering",
  "semester": 6,
  "stream": "BCA",
  "credits": 4,
  "department": "Computer Science & Engineering",
  "description": "SDLC, Agile, Testing",
  "academicYear": "2024-2025"
}