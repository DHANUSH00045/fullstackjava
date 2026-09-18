package com.jobportal.controller;

import com.jobportal.dto.UserProfileDto;
import com.jobportal.dto.UserRegistrationDto;
import com.jobportal.model.User;
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

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Login View
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        return "auth/login";
    }

    // Registration View
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register";
    }

    // Process Registration
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Account created successfully! Please sign in with your credentials.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "auth/register";
        }
    }

    // Profile View & Edit
    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";
        User user = userService.getUserByEmail(authentication.getName());

        UserProfileDto dto = new UserProfileDto();
        dto.setFullName(user.getFullName());
        dto.setCompanyOrCollege(user.getCompanyOrCollege());
        dto.setPhone(user.getPhone());
        dto.setSkills(user.getSkills());
        dto.setBio(user.getBio());

        model.addAttribute("user", user);
        model.addAttribute("profileDto", dto);
        return "auth/profile";
    }

    // Update Profile with Resume Upload
    @PostMapping("/profile/update")
    public String updateProfile(
            @Valid @ModelAttribute("profileDto") UserProfileDto profileDto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null) return "redirect:/login";
        User user = userService.getUserByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "auth/profile";
        }

        userService.updateUserProfile(user.getId(), profileDto);
        redirectAttributes.addFlashAttribute("successMessage", "Profile and resume updated successfully!");
        return "redirect:/profile";
    }

    // Download Resume File
    @GetMapping("/resume/download/{userId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user.getResumeData() == null || user.getResumeFileName() == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(user.getResumeData());
        String contentType = user.getResumeContentType() != null ? user.getResumeContentType() : "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + user.getResumeFileName() + "\"")
                .body(resource);
    }
}
