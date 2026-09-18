package com.jobportal.dto;

import org.springframework.web.multipart.MultipartFile;
import javax.validation.constraints.NotNull;

public class ApplicationDto {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    private String coverLetter;

    private MultipartFile resumeFile;

    public ApplicationDto() {}

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }

    public MultipartFile getResumeFile() { return resumeFile; }
    public void setResumeFile(MultipartFile resumeFile) { this.resumeFile = resumeFile; }
}
