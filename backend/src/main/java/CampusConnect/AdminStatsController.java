package CampusConnect;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AdminStatsController {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final ResourceRepository resourceRepository;
    private final SemesterRepository semesterRepository;

    public AdminStatsController(
            UserRepository userRepository,
            SubjectRepository subjectRepository,
            ResourceRepository resourceRepository,
            SemesterRepository semesterRepository) {

        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.resourceRepository = resourceRepository;
        this.semesterRepository = semesterRepository;
    }

    @GetMapping("/api/admin/stats")
    public AdminStats getStats() {

        return new AdminStats(
                userRepository.count(),
                subjectRepository.count(),
                resourceRepository.count(),
                semesterRepository.count()
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