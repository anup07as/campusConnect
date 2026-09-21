package CampusConnect;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "opportunities")
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String type;

    private String role;

    private String location;

    @Column(name = "work_mode")
    private String workMode;

    private String eligibility;

    private String cgpa;

    private String skills;

    private String stipend;

    private String salary;

    private LocalDate deadline;

    @Column(name = "apply_url")
    private String applyUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    public Opportunity() {
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


    public void setCompany(String company) {
        this.company = company;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setStipend(String stipend) {
        this.stipend = stipend;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}