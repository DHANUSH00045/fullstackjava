package com.jobportal.dto;

import javax.validation.constraints.*;

public class JobPostDto {

    private Long id;

    @NotBlank(message = "Job title is mandatory")
    @Size(min = 3, max = 150, message = "Job title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Company name is mandatory")
    private String companyName;

    @NotBlank(message = "Location is mandatory (e.g. Bengaluru, Remote)")
    private String location;

    @NotBlank(message = "Job category is mandatory")
    private String category;

    @NotBlank(message = "Job type is mandatory (e.g. Full-Time, Internship)")
    private String jobType;

    @NotBlank(message = "Experience level is mandatory")
    private String experienceLevel;

    @NotBlank(message = "Salary range is mandatory (e.g. ₹10 - ₹15 LPA)")
    private String salary;

    @NotBlank(message = "Job description is mandatory")
    @Size(min = 20, max = 4000, message = "Job description must be between 20 and 4000 characters")
    private String description;

    @NotBlank(message = "Required skills are mandatory (e.g. Java, Spring Boot, React)")
    private String skillsRequired;

    public JobPostDto() {}

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
}
