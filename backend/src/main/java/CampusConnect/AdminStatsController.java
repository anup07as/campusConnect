package CampusConnect;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@CrossOrigin(origins = { "https://campusconnect-web-me6v.onrender.com", "https://campusconnect-dva.pages.dev" })
public class AdminStatsController {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ResourceRepository resourceRepository;
    private final SemesterRepository semesterRepository;
    private final AdminAuthService adminAuthService;

    public AdminStatsController(
            UserRepository userRepository,
            SubjectRepository subjectRepository,
            ResourceRepository resourceRepository,
            SemesterRepository semesterRepository,
            AdminAuthService adminAuthService) {

        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.resourceRepository = resourceRepository;
        this.semesterRepository = semesterRepository;
        this.adminAuthService = adminAuthService;
    }

    @GetMapping("/api/admin/stats")
public ResponseEntity<?> getStats(
        @RequestHeader(
                value = "Authorization",
                required = false)
        String authorizationHeader) {

    if (!adminAuthService.isAdmin(authorizationHeader)) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Admin access required");
    }
    return ResponseEntity.ok(
            new AdminStats(
                userRepository.count(),
                subjectRepository.count(),
                resourceRepository.count(),
             semesterRepository.count()
            )
    );
    }

    public static class AdminStats {

        private long students;
        private long subjects;
        private long resources;
        private long semesters;

        public AdminStats(
                long students,
                long subjects,
                long resources,
                long semesters) {

            this.students = students;
            this.subjects = subjects;
            this.resources = resources;
            this.semesters = semesters;
        }

        public long getStudents() {
            return students;
        }

        public long getSubjects() {
            return subjects;
        }

        public long getResources() {
            return resources;
        }

        public long getSemesters() {
            return semesters;
        }
    }
}