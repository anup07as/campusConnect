package CampusConnect;

import java.time.LocalDateTime;

public class SavedResourceResponse {

    private Long resourceId;
    private String resourceTitle;
    private String subjectName;
    private String filePath;
    private LocalDateTime savedAt;

    public SavedResourceResponse(
            Long resourceId,
            String resourceTitle,
            String subjectName,
            String filePath,
            LocalDateTime savedAt) {

        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.subjectName = subjectName;
        this.filePath = filePath;
        this.savedAt = savedAt;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }
}