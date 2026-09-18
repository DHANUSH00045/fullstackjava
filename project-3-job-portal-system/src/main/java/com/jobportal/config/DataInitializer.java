package com.jobportal.config;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, JobRepository jobRepository,
                           ApplicationRepository applicationRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) return;

        // 1. Seed Demo Users
        User student = new User("Aarav Sharma", "student@jobportal.com", passwordEncoder.encode("student123"), "ROLE_STUDENT", "VTU Campus - CSE", "+91 9876543210");
        student.setSkills("Java, Spring Boot, React, SQL, Git, REST APIs");
        student.setBio("Passionate final year Computer Science student seeking full-stack software development opportunities.");

        User employer = new User("Google Cloud Hiring", "employer@google.com", passwordEncoder.encode("employer123"), "ROLE_EMPLOYER", "Google Cloud India", "+91 8012345678");
        employer.setBio("Google Cloud platform engineering and solutions development teams hiring fresh graduates and experienced engineers.");

        User admin = new User("System Administrator", "admin@jobportal.com", passwordEncoder.encode("admin123"), "ROLE_ADMIN", "VTU Placement Central Cell", "+91 9999988888");

        userRepository.save(student);
        userRepository.save(employer);
        userRepository.save(admin);

        // 2. Seed Demo Jobs
        Job j1 = new Job(
                "Full Stack Java Developer",
                "Google Cloud India",
                "Bengaluru, Karnataka",
                "Software Engineering",
                "Full-Time",
                "Fresher / Entry Level",
                "₹14 - ₹18 LPA",
                "We are seeking innovative full-stack software engineers to build scalable cloud microservices, reactive user interfaces, and robust RESTful APIs using Spring Boot and React.",
                "Java 17, Spring Boot, React JS, PostgreSQL, Docker",
                employer
        );

        Job j2 = new Job(
                "Associate AI/ML Engineer",
                "DeepMind Labs India",
                "Hyderabad / Remote",
                "Data Science & AI",
                "Full-Time",
                "1-3 Years",
                "₹18 - ₹24 LPA",
                "Work on state-of-the-art multimodal LLMs, agentic workflow pipelines, and vector database retrieval systems (RAG).",
                "Python, PyTorch, LangChain, Transformers, FastAPI",
                employer
        );

        Job j3 = new Job(
                "Cloud DevOps & SRE Intern",
                "Amazon Web Services (AWS)",
                "Bengaluru, Karnataka",
                "Cloud & DevOps",
                "Internship",
                "Fresher / Student",
                "₹45,000 / month Stipend",
                "6-month paid internship working on automated CI/CD pipelines, Kubernetes container orchestration, and Terraform infrastructure-as-code.",
                "Linux, Docker, Kubernetes, GitHub Actions, AWS",
                employer
        );

        Job j4 = new Job(
                "Cybersecurity Penetration Tester",
                "CrowdStrike Security",
                "Pune / Hybrid",
                "Cybersecurity",
                "Full-Time",
                "1-3 Years",
                "₹12 - ₹16 LPA",
                "Conduct vulnerability assessments, web application penetration testing, and collaborate with dev teams on secure coding guidelines.",
                "OWASP Top 10, Burp Suite, Network Forensics, Python",
                employer
        );

        jobRepository.save(j1);
        jobRepository.save(j2);
        jobRepository.save(j3);
        jobRepository.save(j4);

        // 3. Seed Demo Applications
        Application app1 = new Application(
                student,
                j1,
                "Dear Hiring Manager,\n\nI am thrilled to apply for the Full Stack Java Developer role at Google Cloud. I have extensive hands-on experience building Spring Boot and React fullstack projects.",
                "Aarav_Sharma_Resume.pdf",
                "application/pdf",
                "DEMO_RESUME_PDF_CONTENT".getBytes()
        );
        app1.setStatus("SHORTLISTED");

        Application app2 = new Application(
                student,
                j3,
                "Dear AWS Team,\n\nI am eager to contribute to cloud infrastructure automation. I hold AWS Cloud Practitioner certification and have deployed Kubernetes clusters.",
                "Aarav_Sharma_Resume.pdf",
                "application/pdf",
                "DEMO_RESUME_PDF_CONTENT".getBytes()
        );
        app2.setStatus("PENDING");

        applicationRepository.save(app1);
        applicationRepository.save(app2);
    }
}
