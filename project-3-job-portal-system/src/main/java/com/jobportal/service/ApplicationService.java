package com.jobportal.service;

import com.jobportal.dto.ApplicationDto;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.exception.StorageException;
import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
    }

    public Application applyForJob(ApplicationDto dto, User applicant) {
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + dto.getJobId()));

        if (applicationRepository.existsByApplicantIdAndJobId(applicant.getId(), job.getId())) {
            throw new IllegalArgumentException("You have already submitted an application for this position!");
        }

        String resumeFileName = null;
        String resumeContentType = null;
        byte[] resumeData = null;

        MultipartFile resumeFile = dto.getResumeFile();
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                resumeFileName = resumeFile.getOriginalFilename();
                resumeContentType = resumeFile.getContentType();
                resumeData = resumeFile.getBytes();
            } catch (IOException e) {
                throw new StorageException("Could not process attached resume file: " + e.getMessage(), e);
            }
        } else if (applicant.getResumeData() != null) {
            // Use existing profile resume
            resumeFileName = applicant.getResumeFileName();
            resumeContentType = applicant.getResumeContentType();
            resumeData = applicant.getResumeData();
        }

        Application application = new Application(
                applicant,
                job,
                dto.getCoverLetter(),
                resumeFileName,
                resumeContentType,
                resumeData
        );

        return applicationRepository.save(application);
    }

    public List<Application> getStudentApplications(Long studentId) {
        return applicationRepository.findByApplicantIdOrderByAppliedDateDesc(studentId);
    }

    public List<Application> getEmployerApplications(Long employerId) {
        return applicationRepository.findByJobEmployerIdOrderByAppliedDateDesc(employerId);
    }

    public List<Application> getJobApplications(Long jobId) {
        return applicationRepository.findByJobIdOrderByAppliedDateDesc(jobId);
    }

    public Application getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));
    }

    public Application updateStatus(Long applicationId, String status, User employer) {
        Application application = getApplicationById(applicationId);
        // Verify permissions
        if (!application.getJob().getEmployer().getId().equals(employer.getId()) && !"ROLE_ADMIN".equals(employer.getRole())) {
            throw new SecurityException("You do not have permission to review this applicant.");
        }
        application.setStatus(status.toUpperCase());
        return applicationRepository.save(application);
    }

    public boolean hasApplied(Long applicantId, Long jobId) {
        return applicationRepository.existsByApplicantIdAndJobId(applicantId, jobId);
    }

    public Long getTotalApplicationsCount() {
        return applicationRepository.countTotalApplications();
    }

    public Long getApplicationsCountForEmployer(Long employerId) {
        return applicationRepository.countApplicationsForEmployer(employerId);
    }

    public Long getShortlistedCountForEmployer(Long employerId) {
        return applicationRepository.countShortlistedForEmployer(employerId);
    }
}
