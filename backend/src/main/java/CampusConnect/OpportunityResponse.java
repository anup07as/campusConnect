package CampusConnect;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OpportunityResponse {

    private Long id;
    private String company;
    private String title;
    private String type;
    private String role;
    private String location;
    private String workMode;
    private String eligibility;
    private String cgpa;
    private String skills;
    private String stipend;
    private String salary;
    private LocalDate deadline;
    private String applyUrl;
    private String description;
    private String companyLogo;
    private String status;
    private LocalDateTime createdAt;

    public OpportunityResponse(
            Long id,
            String company,
            String title,
            String type,
            String role,
            String location,
            String workMode,
            String eligibility,
            String cgpa,
            String skills,
            String stipend,
            String salary,
            LocalDate deadline,
            String applyUrl,
            String description,
            String companyLogo,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.company = company;
        this.title = title;
        this.type = type;
        this.role = role;
        this.location = location;
        this.workMode = workMode;
        this.eligibility = eligibility;
        this.cgpa = cgpa;
        this.skills = skills;
        this.stipend = stipend;
        this.salary = salary;
        this.deadline = deadline;
        this.applyUrl = applyUrl;
        this.description = description;
        this.companyLogo = companyLogo;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getRole() {
        return role;
    }

    public String getLocation() {
        return location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getEligibility() {
        return eligibility;
    }

    public String getCgpa() {
        return cgpa;
    }

    public String getSkills() {
        return skills;
    }

    public String getStipend() {
        return stipend;
    }

    public String getSalary() {
        return salary;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}