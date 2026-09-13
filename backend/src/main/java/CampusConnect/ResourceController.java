package CampusConnect;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ResourceController {
    private final ResourceRepository resourceRepository;
    private final AdminAuthService adminAuthService;
    public ResourceController(
            ResourceRepository resourceRepository,
            AdminAuthService adminAuthService) {

        this.resourceRepository = resourceRepository;
        this.adminAuthService = adminAuthService;
    }
    // GET RESOURCES
    @GetMapping("/api/resources")
    public List<Resource> getResources(
            @RequestParam Long subjectId) {

        return resourceRepository.findBySubjectId(subjectId);
    }
    // ADD RESOURCE
    @PostMapping("/api/resources")
    public ResponseEntity<?> addResource(
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("subjectId") Long subjectId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader)
            throws IOException {
        if (!adminAuthService.isAdmin(authorizationHeader)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }
        Path uploadPath =
                Paths.get("uploads");
        if (!Files.exists(uploadPath)) {

            Files.createDirectories(uploadPath);
        }
        String fileName =
                file.getOriginalFilename();
        Path filePath =
                uploadPath.resolve(fileName);
        Files.copy(
                file.getInputStream(),
                filePath
        );
        Resource resource =
                new Resource(
                        title,
                        type,
                        "http://127.0.0.1:8080/uploads/"
                                + fileName,
                        subjectId
                );
        return ResponseEntity.ok(
                resourceRepository.save(resource)
        );
    }
    // DELETE RESOURCE
    @DeleteMapping("/api/resources/{id}")
    public ResponseEntity<?> deleteResource(
            @PathVariable Long id,
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader) {
        if (!adminAuthService.isAdmin(authorizationHeader)) {
          return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }
        Resource resource =
                resourceRepository
                        .findById(id)
                        .orElse(null);
        if (resource == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
        try {
            String fileUrl =
                    resource.getFilePath();
            String fileName =
                    fileUrl.substring(
                            fileUrl.lastIndexOf("/") + 1
                    );
            Path filePath =
                    Paths.get("uploads")
                            .resolve(fileName);
            Files.deleteIfExists(filePath);

        } catch (Exception e) {

            e.printStackTrace();
        }
        resourceRepository.deleteById(id);
        return ResponseEntity.ok(
                "Resource deleted successfully"
        );
    }
    // EDIT RESOURCE
    @PutMapping("/api/resources/{id}")
    public ResponseEntity<?> updateResource(
            @PathVariable Long id,
            @RequestBody Resource updatedResource,
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader) {
        if (!adminAuthService.isAdmin(authorizationHeader)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }
        Resource resource =
                resourceRepository
                        .findById(id)
                        .orElse(null);
        if (resource == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        resource.setTitle(
                updatedResource.getTitle()
        );
        resource.setType(
                updatedResource.getType()
        );
        return ResponseEntity.ok(
                resourceRepository.save(resource)
        );
    }
}