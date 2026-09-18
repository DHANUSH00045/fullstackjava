package com.jobportal.controller;

import com.jobportal.dto.ApplicationDto;
import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class JobSeekerController {

    private final JobService jobService;
    private final ApplicationService applicationService;
    private final UserService userService;

    @Autowired
    public JobSeekerController(JobService jobService, ApplicationService applicationService, UserService userService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.userService = userService;
    }

    // Landing Page
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("jobs", jobService.getActiveJobs());
        model.addAttribute("totalJobs", jobService.countActiveJobs());
        model.addAttribute("categories", jobService.getJobCountsByCategory());
        return "index";
    }

    // Job Search & Filtering
    @GetMapping("/jobs")
    public String searchJobs(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "experienceLevel", required = false) String experienceLevel,
            Model model
    ) {
        List<Job> jobs;
        if (keyword != null || category != null || location != null || experienceLevel != null) {
            jobs = jobService.searchAndFilter(keyword, category, location, experienceLevel);
        } else {
            jobs = jobService.getActiveJobs();
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("selectedExp", experienceLevel);

        return "jobs/search";
    }

    // Single Job Details View
    @GetMapping("/jobs/{id}")
    public String viewJobDetails(@PathVariable("id") Long id, Authentication authentication, Model model) {
        Job job = jobService.getJobById(id);
        boolean alreadyApplied = false;
        User currentUser = null;

        if (authentication != null) {
            currentUser = userService.getUserByEmail(authentication.getName());
            alreadyApplied = applicationService.hasApplied(currentUser.getId(), job.getId());
        }

        ApplicationDto appDto = new ApplicationDto();
        appDto.setJobId(job.getId());

        model.addAttribute("job", job);
        model.addAttribute("alreadyApplied", alreadyApplied);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("applicationDto", appDto);

        return "jobs/detail";
    }

    // Apply for a Job
    @PostMapping("/jobs/apply")
    public String applyForJob(
            @Valid @ModelAttribute("applicationDto") ApplicationDto applicationDto,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null) {
            return "redirect:/login";
        }

        User student = userService.getUserByEmail(authentication.getName());

        try {
            applicationService.applyForJob(applicationDto, student);
            redirectAttributes.addFlashAttribute("successMessage", "Application submitted successfully! Track your status below.");
            return "redirect:/my-applications";
        } catch (IllegalArgumentException | SecurityException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/jobs/" + applicationDto.getJobId();
        }
    }

    // My Applications Dashboard for Student
    @GetMapping("/my-applications")
    public String myApplications(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";
        User student = userService.getUserByEmail(authentication.getName());

        List<Application> applications = applicationService.getStudentApplications(student.getId());
        model.addAttribute("applications", applications);
        model.addAttribute("user", student);

        return "jobs/my-applications";
    }
}
