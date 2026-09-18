# Project 3: Job Portal Management System

A full-stack enterprise web application built with **Spring Boot**, **Spring Security**, **Spring Data JPA**, and **Thymeleaf** that seamlessly connects students/job seekers with employers and placement coordinators.

---

## Technical Highlights
- **Spring Boot & MVC**: Annotation-based controllers (`@Controller`, `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@ModelAttribute`, `@PathVariable`, `@RequestParam`).
- **Spring Security & Authentication**:
  - Role-Based Access Control (`ROLE_STUDENT`, `ROLE_EMPLOYER`, `ROLE_ADMIN`).
  - Strong password hashing with `BCryptPasswordEncoder`.
  - Custom `UserDetailsService` and customized login/registration flows.
  - Context-aware post-login redirects (Employers → `/employer/dashboard`, Students → `/jobs`, Admins → `/admin/dashboard`).
- **Spring Data JPA & Hibernate**:
  - Entity Modeling (`User`, `Job`, `Application`).
  - Relationships: One-to-Many (`Employer → Jobs`), Many-to-One (`Application → Job/User`).
  - Custom queries with sorting and multi-criteria filters.
- **Form Validation**: Strict validation with `@NotBlank`, `@NotNull`, `@Size`, `@Email`, and `BindingResult`.
- **Global Centralized Exception Handling**: `@ControllerAdvice` for graceful error rendering and REST error payloads.
- **Multipart File Storage**: Direct file uploading, storing, and binary streaming of student resumes using `MultipartFile` and `@Lob byte[]`.
- **Database**: Embedded zero-configuration H2 database (with web console at `/h2-console`) and production-ready MySQL profiles.
- **Frontend**: Responsive modern dark glassmorphic UI with HTML5, CSS3, ES6 JavaScript, and Thymeleaf template fragments.

---

## Key Features by User Role

### 1. Job Seeker / Student Account
- **User Profile & Resume**: Maintain skills, bio, phone, college info, and upload resume files (`.pdf`, `.docx`, `.txt`).
- **Search & Filter Jobs**: Filter jobs by keywords, category (Software, AI/ML, Cloud/DevOps, Cybersecurity), location, and experience level.
- **Apply for Jobs**: Submit applications with personalized cover letters and attached resumes.
- **Application Tracker**: Live dashboard to monitor real-time review progress (`PENDING`, `SHORTLISTED`, `REJECTED`) with shortlist alerts.

### 2. Employer / Recruiter Account
- **Analytics Dashboard**: Real-time telemetry on active job openings, total applicants, and shortlisted candidate metrics.
- **Job Lifecycle Management (CRUD)**:
  - Post new jobs with role descriptions, compensation, and required skills.
  - Edit or update existing postings.
  - Delete expired openings.
- **Applicant Review & Shortlisting**:
  - View applicant roster with skills summary.
  - One-click resume download.
  - Instant status action buttons: **Shortlist** or **Reject**.

### 3. Admin Account
- **Platform Telemetry**: Comprehensive counts for total users, active listings, and applications.
- **User Directory**: View registered students and employers with contact information.
- **Job Catalog Governance**: Oversee all published jobs across the portal.

---

## Pre-Configured Demo Credentials

| Role | Email | Password | Role Key |
|---|---|---|---|
| **Student / Job Seeker** | `student@jobportal.com` | `student123` | `ROLE_STUDENT` |
| **Employer / Recruiter** | `employer@google.com` | `employer123` | `ROLE_EMPLOYER` |
| **System Administrator** | `admin@jobportal.com` | `admin123` | `ROLE_ADMIN` |

*You can also register a new Student or Employer account at `/register`.*

---

## REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/jobs` | Retrieve all active jobs (supports `?keyword=`, `?category=`, `?location=`, `?experienceLevel=`) |
| `GET` | `/api/jobs/{id}` | Get specific job details by ID |
| `POST` | `/api/jobs?employerId={id}` | Post a new job via JSON body |
| `GET` | `/api/applications/student/{id}` | Get all applications for a student |
| `GET` | `/api/applications/job/{jobId}` | Get all applications submitted for a job |
| `POST` | `/api/applications?studentId={id}` | Submit a job application |

---

## How to Run Locally

### Option A: Using Maven CLI / IDE
1. Navigate to the project directory:
   ```bash
   cd project-3-job-portal-system
   ```
2. Run the Spring Boot application:
   ```bash
   mvn clean spring-boot:run
   ```
3. Open your browser:
   - Web App: `http://localhost:8082`
   - Sign In: `http://localhost:8082/login`
   - H2 Console: `http://localhost:8082/h2-console` (JDBC URL: `jdbc:h2:mem:jobportaldb`, User: `sa`, Password: empty)
