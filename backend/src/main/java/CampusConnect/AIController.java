package CampusConnect;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://campusconnect-web-me6v.onrender.com")
@RequestMapping("/api/ai")
public class AIController {

    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(
            @RequestBody Map<String, Object> request) {

        Object historyObject = request.get("history");

        if (!(historyObject instanceof List)) {
            return Map.of(
                    "answer",
                    "Please start a new conversation."
            );
        }

        List<Map<String, String>> history =
                (List<Map<String, String>>) historyObject;

        if (history.isEmpty()) {
            return Map.of(
                    "answer",
                    "Please enter a question."
            );
        }

        String answer =
                geminiService.askGemini(history);

        return Map.of(
                "answer",
                answer
        );
    }
}