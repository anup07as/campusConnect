package CampusConnect;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(length = 500)
    private String technologies;
    @Column(name = "github_url", length = 500)
    private String githubUrl;
    @Column(name = "live_demo_url", length = 500)
    private String liveDemoUrl;
    @Column(name = "image_path", length = 500)
    private String imagePath;
    @Column(nullable = false)
    private String status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    public Project() {
    }
    public Long getId() {
        return id;
    }
    public Long getUserId() {
        return userId;
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }
    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }
    public void setLiveDemoUrl(String liveDemoUrl) {
        this.liveDemoUrl = liveDemoUrl;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}