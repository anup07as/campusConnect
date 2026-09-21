package CampusConnect;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "saved_resources",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"user_id", "resource_id"}
        )
    }
)
public class SavedResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;


    public SavedResource() {
    }


    public Long getId() {
        return id;
    }


    public Long getUserId() {
        return userId;
    }


    public Long getResourceId() {
        return resourceId;
    }


    public LocalDateTime getSavedAt() {
        return savedAt;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }


    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}