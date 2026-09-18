package com.jobportal.controller.api;

import com.jobportal.dto.JobPostDto;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.service.JobService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobApiController {

    private final JobService jobService;
    private final UserService userService;

    @Autowired
    public JobApiController(JobService jobService, UserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    // GET /api/jobs
    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String experienceLevel
    ) {
        if (keyword != null || category != null || location != null || experienceLevel != null) {
            return ResponseEntity.ok(jobService.searchAndFilter(keyword, category, location, experienceLevel));
        }
        return ResponseEntity.ok(jobService.getActiveJobs());
    }

    // GET /api/jobs/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // POST /api/jobs (Pass employerId as query or header)
    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody JobPostDto jobDto, @RequestParam Long employerId) {
        User employer = userService.getUserById(employerId);
        Job created = jobService.postJob(jobDto, employer);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
