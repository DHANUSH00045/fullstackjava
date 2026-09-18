package com.jobportal.service;

import com.jobportal.dto.UserProfileDto;
import com.jobportal.dto.UserRegistrationDto;
import com.jobportal.exception.ResourceNotFoundException;
import com.jobportal.exception.StorageException;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegistrationDto dto) {
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("An account with email " + dto.getEmail() + " already exists!");
        }

        String formattedRole = dto.getRole().toUpperCase();
        if (!formattedRole.startsWith("ROLE_")) {
            formattedRole = "ROLE_" + formattedRole;
        }

        User user = new User(
                dto.getFullName().trim(),
                dto.getEmail().trim().toLowerCase(),
                passwordEncoder.encode(dto.getPassword()),
                formattedRole,
                dto.getCompanyOrCollege(),
                dto.getPhone()
        );

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserProfile(Long userId, UserProfileDto dto) {
        User user = getUserById(userId);
        user.setFullName(dto.getFullName());
        user.setCompanyOrCollege(dto.getCompanyOrCollege());
        user.setPhone(dto.getPhone());
        user.setSkills(dto.getSkills());
        user.setBio(dto.getBio());

        // Handle Resume Upload if present
        MultipartFile resumeFile = dto.getResumeFile();
        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                user.setResumeFileName(resumeFile.getOriginalFilename());
                user.setResumeContentType(resumeFile.getContentType());
                user.setResumeData(resumeFile.getBytes());
            } catch (IOException e) {
                throw new StorageException("Failed to upload resume file: " + e.getMessage(), e);
            }
        }

        return userRepository.save(user);
    }

    public User updateProfile(Long userId, UserProfileDto dto) {
        return updateUserProfile(userId, dto);
    }
}
