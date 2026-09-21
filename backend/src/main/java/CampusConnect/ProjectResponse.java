package CampusConnect;

import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String studentName;
    private String title;
    private String description;
    private String technologies;
    private String githubUrl;
    private String liveDemoUrl;
    private String imagePath;
    private String status;
    private LocalDateTime createdAt;

    public ProjectResponse(
            Long id,
            String studentName,
            String title,
            String description,
            String technologies,
            String githubUrl,
            String liveDemoUrl,
            String imagePath,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.studentName = studentName;
        this.title = title;
        this.description = description;
        this.technologies = technologies;
        this.githubUrl = githubUrl;
        this.liveDemoUrl = liveDemoUrl;
        this.imagePath = imagePath;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public String getLiveDemoUrl() {
        return liveDemoUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}