package CampusConnect;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SavedResourceController {

    private final SavedResourceRepository savedResourceRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final SubjectRepository subjectRepository;

 public SavedResourceController(
        SavedResourceRepository savedResourceRepository,
        UserRepository userRepository,
        ResourceRepository resourceRepository,
        SubjectRepository subjectRepository) {

        this.savedResourceRepository = savedResourceRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
        this.subjectRepository = subjectRepository;
    }


    // ===============================
    // SAVE RESOURCE
    // ===============================

    @PostMapping("/api/saved-resources/{resourceId}")
    public ResponseEntity<?> saveResource(
            @PathVariable Long resourceId,
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader) {

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        String token =
                authorizationHeader.substring(7);

        User user =
                userRepository.findByToken(token);

        if (user == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid session");
        }

        if (!resourceRepository.existsById(resourceId)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Resource not found");
        }

        if (savedResourceRepository
                .existsByUserIdAndResourceId(
                        user.getId(),
                        resourceId)) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Resource already saved");
        }

        SavedResource savedResource =
                new SavedResource();

        savedResource.setUserId(
                user.getId()
        );

        savedResource.setResourceId(
                resourceId
        );

        savedResource.setSavedAt(
                LocalDateTime.now()
        );

        savedResourceRepository.save(
                savedResource
        );

        return ResponseEntity.ok(
                "Resource saved successfully"
        );
    }


   // ===============================
// GET SAVED RESOURCES
// ===============================

@GetMapping("/api/saved-resources")
public ResponseEntity<?> getSavedResources(
        @RequestHeader(
                value = "Authorization",
                required = false
        )
        String authorizationHeader) {

    if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Login required");
    }

    String token =
            authorizationHeader.substring(7);

    User user =
            userRepository.findByToken(token);

    if (user == null) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid session");
    }

    List<SavedResource> savedResources =
            savedResourceRepository.findByUserId(
                    user.getId()
            );

    List<SavedResourceResponse> response =
            savedResources.stream()
                    .map(savedResource -> {

                        Resource resource =
                                resourceRepository
                                        .findById(
                                                savedResource.getResourceId()
                                        )
                                        .orElse(null);

                        if (resource == null) {
                            return null;
                        }

                        Subject subject =
                                subjectRepository
                                        .findById(
                                                resource.getSubjectId()
                                        )
                                        .orElse(null);

                        String subjectName =
                                subject != null
                                        ? subject.getName()
                                        : "Unknown Subject";

                        return new SavedResourceResponse(
                                resource.getId(),
                                resource.getTitle(),
                                subjectName,
                                resource.getFilePath(),
                                savedResource.getSavedAt()
                        );
                    })
                    .filter(item -> item != null)
                    .toList();

    return ResponseEntity.ok(response);
}


    // ===============================
    // REMOVE SAVED RESOURCE
    // ===============================

    @DeleteMapping("/api/saved-resources/{resourceId}")
    public ResponseEntity<?> removeSavedResource(
            @PathVariable Long resourceId,
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader) {

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Login required");
        }

        String token =
                authorizationHeader.substring(7);

        User user =
                userRepository.findByToken(token);

        if (user == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid session");
        }

        if (!savedResourceRepository
                .existsByUserIdAndResourceId(
                        user.getId(),
                        resourceId)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Saved resource not found");
        }

        savedResourceRepository
                .deleteByUserIdAndResourceId(
                        user.getId(),
                        resourceId
                );

        return ResponseEntity.ok(
                "Resource removed from saved resources"
        );
    }
}