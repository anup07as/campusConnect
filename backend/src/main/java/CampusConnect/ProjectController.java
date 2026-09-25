package CampusConnect;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = { "https://campusconnect-web-me6v.onrender.com", "https://campusconnect-dva.pages.dev" })
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public ProjectController(
            ProjectRepository projectRepository,
            UserRepository userRepository) {

        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }


    // ===============================
    // SUBMIT PROJECT
    // ===============================

    @PostMapping(
            value = "/api/projects",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<?> submitProject(

            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader,

            @RequestPart("project")
            Project project,

            @RequestPart(
                    value = "image",
                    required = false
            )
            MultipartFile image) {

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


        try {

            // ===============================
            // SAVE PROJECT IMAGE
            // ===============================

            if (image != null &&
                    !image.isEmpty()) {

                String contentType =
                        image.getContentType();


                if (contentType == null ||
                        (
                            !contentType.equals("image/png") &&
                            !contentType.equals("image/jpeg") &&
                            !contentType.equals("image/webp")
                        )) {

                    return ResponseEntity
                            .badRequest()
                            .body(
                                    "Only JPG, PNG and WebP images are allowed."
                            );
                }


                if (image.getSize() > 5 * 1024 * 1024) {

                    return ResponseEntity
                            .badRequest()
                            .body(
                                    "Image size must be less than 5 MB."
                            );
                }


                Path uploadDirectory =
                        Paths.get("uploads");


                Files.createDirectories(
                        uploadDirectory
                );


                String originalName =
                        image.getOriginalFilename();


                String extension = "";


                if (originalName != null &&
                        originalName.contains(".")) {

                    extension =
                            originalName.substring(
                                    originalName.lastIndexOf(".")
                            );
                }


                String fileName =
                        UUID.randomUUID()
                                .toString()
                                + extension;


                Path filePath =
                        uploadDirectory.resolve(
                                fileName
                        );


                Files.copy(
                        image.getInputStream(),
                        filePath
                );


                project.setImagePath(
                        "/uploads/" + fileName
                );
            }


            // ===============================
            // STUDENT INFORMATION
            // ===============================

            project.setUserId(
                    user.getId()
            );


            project.setStatus(
                    "PENDING"
            );


            project.setCreatedAt(
                    LocalDateTime.now()
            );


            Project savedProject =
                    projectRepository.save(
                            project
                    );


            return ResponseEntity.ok(
                    new ProjectResponse(
                            savedProject.getId(),
                            user.getName(),
                            savedProject.getTitle(),
                            savedProject.getDescription(),
                            savedProject.getTechnologies(),
                            savedProject.getGithubUrl(),
                            savedProject.getLiveDemoUrl(),
                            savedProject.getImagePath(),
                            savedProject.getStatus(),
                            savedProject.getCreatedAt()
                    )
            );


        } catch (IOException e) {

            e.printStackTrace();

            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(
                            "Unable to upload project image."
                    );
        }
    }


    // ===============================
    // GET APPROVED PROJECTS
    // ===============================

    @GetMapping("/api/projects")
    public ResponseEntity<?> getApprovedProjects() {

        List<Project> projects =
                projectRepository
                        .findByStatusOrderByCreatedAtDesc(
                                "APPROVED"
                        );


        List<ProjectResponse> response =
                projects.stream()
                        .map(project -> {

                            User user =
                                    userRepository
                                            .findById(
                                                    project.getUserId()
                                            )
                                            .orElse(null);


                            String studentName =
                                    user != null
                                            ? user.getName()
                                            : "Unknown Student";


                            return new ProjectResponse(
                                    project.getId(),
                                    studentName,
                                    project.getTitle(),
                                    project.getDescription(),
                                    project.getTechnologies(),
                                    project.getGithubUrl(),
                                    project.getLiveDemoUrl(),
                                    project.getImagePath(),
                                    project.getStatus(),
                                    project.getCreatedAt()
                            );
                        })
                        .toList();


        return ResponseEntity.ok(
                response
        );
    }


    // ===============================
    // GET MY PROJECTS
    // ===============================

    @GetMapping("/api/projects/my")
    public ResponseEntity<?> getMyProjects(

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


        List<Project> projects =
                projectRepository
                        .findByUserIdOrderByCreatedAtDesc(
                                user.getId()
                        );


        List<ProjectResponse> response =
                projects.stream()
                        .map(project ->
                                new ProjectResponse(
                                        project.getId(),
                                        user.getName(),
                                        project.getTitle(),
                                        project.getDescription(),
                                        project.getTechnologies(),
                                        project.getGithubUrl(),
                                        project.getLiveDemoUrl(),
                                        project.getImagePath(),
                                        project.getStatus(),
                                        project.getCreatedAt()
                                )
                        )
                        .toList();


        return ResponseEntity.ok(
                response
        );
    }


    // ===============================
    // ADMIN - GET ALL PROJECTS
    // ===============================

    @GetMapping("/api/admin/projects")
    public ResponseEntity<?> getAllProjectsForAdmin(

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


        User admin =
                userRepository.findByToken(token);


        if (admin == null ||
                !"ADMIN".equals(admin.getRole())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }


        List<Project> projects =
                projectRepository.findAll();


        List<ProjectResponse> response =
                projects.stream()
                        .map(project -> {

                            User user =
                                    userRepository
                                            .findById(
                                                    project.getUserId()
                                            )
                                            .orElse(null);


                            String studentName =
                                    user != null
                                            ? user.getName()
                                            : "Unknown Student";


                            return new ProjectResponse(
                                    project.getId(),
                                    studentName,
                                    project.getTitle(),
                                    project.getDescription(),
                                    project.getTechnologies(),
                                    project.getGithubUrl(),
                                    project.getLiveDemoUrl(),
                                    project.getImagePath(),
                                    project.getStatus(),
                                    project.getCreatedAt()
                            );
                        })
                        .toList();


        return ResponseEntity.ok(
                response
        );
    }


    // ===============================
    // ADMIN - APPROVE / REJECT PROJECT
    // ===============================

    @PutMapping("/api/admin/projects/{id}/status")
    public ResponseEntity<?> updateProjectStatus(

            @PathVariable Long id,

            @RequestParam String status,

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


        User admin =
                userRepository.findByToken(token);


        if (admin == null ||
                !"ADMIN".equals(admin.getRole())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }


        if (!status.equals("APPROVED") &&
                !status.equals("REJECTED") &&
                !status.equals("PENDING")) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid project status");
        }


        Project project =
                projectRepository
                        .findById(id)
                        .orElse(null);


        if (project == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Project not found");
        }


        project.setStatus(
                status
        );


        Project updatedProject =
                projectRepository.save(
                        project
                );


        User student =
                userRepository
                        .findById(
                                updatedProject.getUserId()
                        )
                        .orElse(null);


        String studentName =
                student != null
                        ? student.getName()
                        : "Unknown Student";


        return ResponseEntity.ok(
                new ProjectResponse(
                        updatedProject.getId(),
                        studentName,
                        updatedProject.getTitle(),
                        updatedProject.getDescription(),
                        updatedProject.getTechnologies(),
                        updatedProject.getGithubUrl(),
                        updatedProject.getLiveDemoUrl(),
                        updatedProject.getImagePath(),
                        updatedProject.getStatus(),
                        updatedProject.getCreatedAt()
                )
        );
    }
    // ===============================
// DELETE PROJECT - ADMIN ONLY
// ===============================

@DeleteMapping("/api/admin/projects/{id}")
public ResponseEntity<?> deleteProject(
        @PathVariable Long id,
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

    User admin =
            userRepository.findByToken(token);

    if (admin == null ||
            !"ADMIN".equals(admin.getRole())) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Admin access required");
    }

    Project project =
            projectRepository
                    .findById(id)
                    .orElse(null);

    if (project == null) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Project not found");
    }

    // Delete uploaded project image
    if (project.getImagePath() != null) {

        try {

            String fileName =
                    project.getImagePath()
                            .replace("/uploads/", "");

            Path imagePath =
                    Paths.get("uploads")
                            .resolve(fileName)
                            .normalize();

            Files.deleteIfExists(imagePath);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // Delete project from database
    projectRepository.delete(project);

    return ResponseEntity.ok(
            "Project deleted successfully"
    );
}
}