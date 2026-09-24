package CampusConnect;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://campusconnect-web-me6v.onrender.com")
@RequestMapping("/api/ai")
public class AIController {

    private final GeminiService geminiService;
    private final UserRepository userRepository;

    public AIController(
            GeminiService geminiService,
            UserRepository userRepository) {

        this.geminiService = geminiService;
        this.userRepository = userRepository;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader,

            @RequestBody Map<String, Object> request) {

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "answer",
                            "Please login to use CampusAI."
                    ));
        }

        String token =
                authorizationHeader.substring(7);

        User user =
                userRepository.findByToken(token);

        if (user == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "answer",
                            "Your session is invalid. Please login again."
                    ));
        }

        Object historyObject =
                request.get("history");

        if (!(historyObject instanceof List)) {

            return ResponseEntity.ok(
                    Map.of(
                            "answer",
                            "Please start a new conversation."
                    )
            );
        }

        List<Map<String, String>> history =
                (List<Map<String, String>>) historyObject;

        if (history.isEmpty()) {

            return ResponseEntity.ok(
                    Map.of(
                            "answer",
                            "Please enter a question."
                    )
            );
        }

        String answer =
                geminiService.askGemini(history);

        return ResponseEntity.ok(
                Map.of(
                        "answer",
                        answer
                )
        );
    }
}