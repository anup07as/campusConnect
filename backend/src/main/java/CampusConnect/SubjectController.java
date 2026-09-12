package CampusConnect;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @GetMapping("/api/subjects")
    public List<Subject> getSubjects(@RequestParam Long semester) {

        return subjectRepository.findBySemesterId(semester);
        
    }
    @PostMapping("/api/subjects")
public Subject addSubject(@RequestBody Subject subject) {
    return subjectRepository.save(subject);
}
}