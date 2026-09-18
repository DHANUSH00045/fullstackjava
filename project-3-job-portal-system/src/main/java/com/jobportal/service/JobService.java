package com.jobportal.service;

import com.jobportal.dto.JobPostDto;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getActiveJobs() {
        return jobRepository.findByStatusOrderByPostedDateDesc("ACTIVE");
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));
    }

    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerIdOrderByPostedDateDesc(employerId);
    }

    public List<Job> searchAndFilter(String keyword, String category, String location, String experienceLevel) {
        return jobRepository.searchAndFilterJobs(keyword, category, location, experienceLevel);
    }

    public Job postJob(JobPostDto dto, User employer) {
        Job job = new Job(
                dto.getTitle().trim(),
                dto.getCompanyName().trim(),
                dto.getLocation().trim(),
                dto.getCategory().trim(),
                dto.getJobType().trim(),
                dto.getExperienceLevel().trim(),
                dto.getSalary().trim(),
                dto.getDescription().trim(),
                dto.getSkillsRequired().trim(),
                employer
        );
        return jobRepository.save(job);
    }

    public Job updateJob(Long id, JobPostDto dto, User employer) {
        Job job = getJobById(id);
        // Ensure employer owns this job or is admin
        if (!job.getEmployer().getId().equals(employer.getId()) && !"ROLE_ADMIN".equals(employer.getRole())) {
            throw new SecurityException("You do not have permission to edit this job posting.");
        }

        job.setTitle(dto.getTitle());
        job.setCompanyName(dto.getCompanyName());
        job.setLocation(dto.getLocation());
        job.setCategory(dto.getCategory());
        job.setJobType(dto.getJobType());
        job.setExperienceLevel(dto.getExperienceLevel());
        job.setSalary(dto.getSalary());
        job.setDescription(dto.getDescription());
        job.setSkillsRequired(dto.getSkillsRequired());

        return jobRepository.save(job);
    }

    public void deleteJob(Long id, User employer) {
        Job job = getJobById(id);
        if (!job.getEmployer().getId().equals(employer.getId()) && !"ROLE_ADMIN".equals(employer.getRole())) {
            throw new SecurityException("You do not have permission to delete this job posting.");
        }
        jobRepository.delete(job);
    }

    public Long countActiveJobs() {
        return jobRepository.countActiveJobs();
    }

    public List<Object[]> getJobCountsByCategory() {
        return jobRepository.countJobsByCategory();
    }
}
