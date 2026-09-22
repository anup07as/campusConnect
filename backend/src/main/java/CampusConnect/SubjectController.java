package CampusConnect;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://campusconnect-web-me6v.onrender.com")
public class SubjectController {

    private final SubjectRepository subjectRepository;
    private final AdminAuthService adminAuthService;

    public SubjectController(
            SubjectRepository subjectRepository,
            AdminAuthService adminAuthService) {

        this.subjectRepository = subjectRepository;
        this.adminAuthService = adminAuthService;
    }


    // GET SUBJECTS
    @GetMapping("/api/subjects")
    public List<Subject> getSubjects(
            @RequestParam Long semester) {

        return subjectRepository.findBySemesterId(semester);
    }


    // ADD SUBJECT
    @PostMapping("/api/subjects")
    public ResponseEntity<?> addSubject(
            @RequestBody Subject subject,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        if (!adminAuthService.isAdmin(authorizationHeader)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        return ResponseEntity.ok(
                subjectRepository.save(subject)
        );
    }


    // DELETE SUBJECT
    @DeleteMapping("/api/subjects/{id}")
    public ResponseEntity<?> deleteSubject(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        if (!adminAuthService.isAdmin(authorizationHeader)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        if (!subjectRepository.existsById(id)) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        subjectRepository.deleteById(id);

        return ResponseEntity.ok(
                "Subject deleted successfully"
        );
    }


    // EDIT SUBJECT
    @PutMapping("/api/subjects/{id}")
    public ResponseEntity<?> updateSubject(
            @PathVariable Long id,
            @RequestBody Subject updatedSubject,
            @RequestHeader(value = "Authorization", required = false)
            String authorizationHeader) {

        if (!adminAuthService.isAdmin(authorizationHeader)) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        Subject subject =
                subjectRepository.findById(id).orElse(null);

        if (subject == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }

        subject.setName(
                updatedSubject.getName()
        );

        subject.setSemesterId(
                updatedSubject.getSemesterId()
        );

        return ResponseEntity.ok(
                subjectRepository.save(subject)
        );
    }
}