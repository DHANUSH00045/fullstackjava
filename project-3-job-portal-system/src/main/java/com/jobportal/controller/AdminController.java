package com.jobportal.controller;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("jobPortalAdminController")
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;

    @Autowired
    public AdminController(UserService userService, JobService jobService, ApplicationService applicationService) {
        this.userService = userService;
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<User> users = userService.getAllUsers();
        List<Job> jobs = jobService.getActiveJobs();
        Long totalApps = applicationService.getTotalApplicationsCount();

        model.addAttribute("users", users);
        model.addAttribute("jobs", jobs);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalJobs", jobs.size());
        model.addAttribute("totalApplications", totalApps != null ? totalApps : 0);

        return "admin/dashboard";
    }
}
