package com.jobportal.controller.api;

import com.jobportal.dto.ApplicationDto;
import com.jobportal.model.Application;
import com.jobportal.model.User;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationApiController {

    private final ApplicationService applicationService;
    private final UserService userService;

    @Autowired
    public ApplicationApiController(ApplicationService applicationService, UserService userService) {
        this.applicationService = applicationService;
        this.userService = userService;
    }

    // GET /api/applications/student/{studentId}
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Application>> getStudentApplications(@PathVariable Long studentId) {
        return ResponseEntity.ok(applicationService.getStudentApplications(studentId));
    }

    // GET /api/applications/job/{jobId}
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getJobApplications(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getJobApplications(jobId));
    }

    // POST /api/applications - Apply for job
    @PostMapping
    public ResponseEntity<Application> apply(@Valid @ModelAttribute ApplicationDto dto, @RequestParam Long studentId) {
        User student = userService.getUserById(studentId);
        Application created = applicationService.applyForJob(dto, student);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
