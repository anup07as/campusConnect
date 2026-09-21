package CampusConnect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SavedResourceRepository
        extends JpaRepository<SavedResource, Long> {

    List<SavedResource> findByUserId(Long userId);

    boolean existsByUserIdAndResourceId(
            Long userId,
            Long resourceId
    );

   @Transactional
@Modifying
@Query("""
    DELETE FROM SavedResource s
    WHERE s.userId = :userId
    AND s.resourceId = :resourceId
""")
    int deleteByUserIdAndResourceId(
            @Param("userId") Long userId,
            @Param("resourceId") Long resourceId
    );
}