package com.jobportal.controller;

import com.jobportal.dto.JobPostDto;
import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/employer")
public class EmployerController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    @Autowired
    public EmployerController(JobService jobService, ApplicationService applicationService, UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    // Employer Analytics Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User employer = userService.getUserByEmail(authentication.getName());

        List<Job> myJobs = jobService.getJobsByEmployer(employer.getId());
        Long totalApps = applicationService.getApplicationsCountForEmployer(employer.getId());
        Long shortlistedApps = applicationService.getShortlistedCountForEmployer(employer.getId());
        List<Application> recentApplicants = applicationService.getEmployerApplications(employer.getId());

        model.addAttribute("employer", employer);
        model.addAttribute("myJobs", myJobs);
        model.addAttribute("totalJobs", myJobs.size());
        model.addAttribute("totalApplications", totalApps != null ? totalApps : 0);
        model.addAttribute("shortlistedCount", shortlistedApps != null ? shortlistedApps : 0);
        model.addAttribute("recentApplicants", recentApplicants);

        return "employer/dashboard";
    }

    // Form to Post New Job
    @GetMapping("/jobs/new")
    public String postJobForm(Model model) {
        model.addAttribute("jobDto", new JobPostDto());
        model.addAttribute("pageTitle", "Post New Job Opportunity");
        model.addAttribute("isEdit", false);
        return "employer/post-job";
    }

    // Save Job (Create or Edit)
    @PostMapping("/jobs/save")
    public String saveJob(
            @Valid @ModelAttribute("jobDto") JobPostDto jobDto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        User employer = userService.getUserByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", jobDto.getId() != null ? "Edit Job Posting" : "Post New Job Opportunity");
            model.addAttribute("isEdit", jobDto.getId() != null);
            return "employer/post-job";
        }

        if (jobDto.getId() != null) {
            jobService.updateJob(jobDto.getId(), jobDto, employer);
            redirectAttributes.addFlashAttribute("successMessage", "Job posting updated successfully!");
        } else {
            jobService.postJob(jobDto, employer);
            redirectAttributes.addFlashAttribute("successMessage", "New job opportunity published successfully!");
        }

        return "redirect:/employer/dashboard";
    }

    // Edit Job Form
    @GetMapping("/jobs/edit/{id}")
    public String editJobForm(@PathVariable("id") Long id, Authentication authentication, Model model) {
        Job job = jobService.getJobById(id);
        User employer = userService.getUserByEmail(authentication.getName());

        if (!job.getEmployer().getId().equals(employer.getId()) && !"ROLE_ADMIN".equals(employer.getRole())) {
            return "redirect:/employer/dashboard";
        }

        JobPostDto dto = new JobPostDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setCompanyName(job.getCompanyName());
        dto.setLocation(job.getLocation());
        dto.setCategory(job.getCategory());
        dto.setJobType(job.getJobType());
        dto.setExperienceLevel(job.getExperienceLevel());
        dto.setSalary(job.getSalary());
        dto.setDescription(job.getDescription());
        dto.setSkillsRequired(job.getSkillsRequired());

        model.addAttribute("jobDto", dto);
        model.addAttribute("pageTitle", "Edit Job: " + job.getTitle());
        model.addAttribute("isEdit", true);

        return "employer/post-job";
    }

    // Delete Job
    @PostMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable("id") Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        User employer = userService.getUserByEmail(authentication.getName());
        jobService.deleteJob(id, employer);
        redirectAttributes.addFlashAttribute("successMessage", "Job posting deleted successfully!");
        return "redirect:/employer/dashboard";
    }

    // View Applicants for All Employer's Jobs
    @GetMapping("/applicants")
    public String viewApplicants(Authentication authentication, Model model) {
        User employer = userService.getUserByEmail(authentication.getName());
        List<Application> applicants = applicationService.getEmployerApplications(employer.getId());
        model.addAttribute("applicants", applicants);
        return "employer/applicants";
    }

    // View Applicants for Specific Job
    @GetMapping("/jobs/{jobId}/applicants")
    public String viewApplicantsByJob(@PathVariable("jobId") Long jobId, Model model) {
        Job job = jobService.getJobById(jobId);
        List<Application> applicants = applicationService.getJobApplications(jobId);
        model.addAttribute("job", job);
        model.addAttribute("applicants", applicants);
        return "employer/applicants";
    }

    // Update Applicant Status (Shortlist / Reject / Accept)
    @PostMapping("/applications/{appId}/status")
    public String updateApplicationStatus(
            @PathVariable("appId") Long appId,
            @RequestParam("status") String status,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        User employer = userService.getUserByEmail(authentication.getName());
        applicationService.updateStatus(appId, status, employer);
        redirectAttributes.addFlashAttribute("successMessage", "Applicant status updated to: " + status);
        return "redirect:/employer/applicants";
    }

    // Download Applicant Resume
    @GetMapping("/applications/{appId}/resume")
    public ResponseEntity<Resource> downloadApplicantResume(@PathVariable("appId") Long appId) {
        Application app = applicationService.getApplicationById(appId);
        if (app.getResumeData() == null || app.getResumeFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(app.getResumeData());
        String contentType = app.getResumeContentType() != null ? app.getResumeContentType() : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + app.getResumeFileName() + "\"")
                .body(resource);
    }
}
