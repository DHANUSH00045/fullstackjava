package com.jobportal.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String location; // e.g. Bengaluru, Remote, Hyderabad

    @Column(nullable = false)
    private String category; // Software Engineering, Data Science & AI, Cloud & DevOps, Cybersecurity

    @Column(name = "job_type", nullable = false)
    private String jobType; // Full-Time, Internship, Part-Time, Remote

    @Column(name = "experience_level", nullable = false)
    private String experienceLevel; // Fresher / 0-1 Years, 1-3 Years, 3-5 Years

    @Column(nullable = false)
    private String salary; // e.g. ₹8 - ₹12 LPA or ₹25,000/month stipend

    @Column(length = 4000, nullable = false)
    private String description;

    @Column(name = "skills_required", nullable = false)
    private String skillsRequired; // e.g. Java, Spring Boot, React, SQL

    @Column(name = "posted_date", nullable = false)
    private LocalDate postedDate = LocalDate.now();

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, CLOSED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private User employer;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications = new ArrayList<>();

    public Job() {}

    public Job(String title, String companyName, String location, String category, String jobType,
               String experienceLevel, String salary, String description, String skillsRequired, User employer) {
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.category = category;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.salary = salary;
        this.description = description;
        this.skillsRequired = skillsRequired;
        this.employer = employer;
        this.postedDate = LocalDate.now();
        this.status = "ACTIVE";
    }

    public int getApplicantCount() {
        return applications != null ? applications.size() : 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }

    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSkillsRequired() { return skillsRequired; }
    public void setSkillsRequired(String skillsRequired) { this.skillsRequired = skillsRequired; }

    public LocalDate getPostedDate() { return postedDate; }
    public void setPostedDate(LocalDate postedDate) { this.postedDate = postedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getEmployer() { return employer; }
    public void setEmployer(User employer) { this.employer = employer; }

    public List<Application> getApplications() { return applications; }
    public void setApplications(List<Application> applications) { this.applications = applications; }
}
