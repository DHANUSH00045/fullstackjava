package com.jobportal.dto;

import javax.validation.constraints.*;

public class UserRegistrationDto {

    @NotBlank(message = "Full Name is mandatory")
    @Size(min = 3, max = 80, message = "Full Name must be between 3 and 80 characters")
    private String fullName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please select an account role")
    private String role; // STUDENT, EMPLOYER

    private String companyOrCollege;
    private String phone;

    public UserRegistrationDto() {}

    // Getters and Setters
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getCompanyOrCollege() { return companyOrCollege; }
    public void setCompanyOrCollege(String companyOrCollege) { this.companyOrCollege = companyOrCollege; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
