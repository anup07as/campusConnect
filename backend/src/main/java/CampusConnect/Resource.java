package CampusConnect;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String type;

    private String filePath;

    private Long subjectId;

    public Resource() {
    }

    public Resource(String title, String type, String filePath, Long subjectId) {
        this.title = title;
        this.type = type;
        this.filePath = filePath;
        this.subjectId = subjectId;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }
}