package CampusConnect;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ResourceRepository
        extends JpaRepository<Resource, Long> {
    List<Resource> findBySubjectId(Long subjectId);
    List<Resource> findByTitleContainingIgnoreCase(String title);
}