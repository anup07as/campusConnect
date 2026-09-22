package CampusConnect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {
@Value("${gemini.api.key}")
private String apiKey;

    private final RestTemplate restTemplate =
            new RestTemplate();

    public String askGemini(
            List<Map<String, String>> history) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        // Convert chat history into Gemini format
        Object[] contents = history.stream()
                .map(message -> Map.of(
                        "role",
                        message.get("role"),
                        "parts",
                        new Object[]{
                                Map.of(
                                        "text",
                                        message.get("text")
                                )
                        }
                ))
                .toArray();

        Map<String, Object> body = Map.of(
                "contents",
                contents
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        // Try up to 2 times
        for (int attempt = 1; attempt <= 2; attempt++) {

            try {

                Map response =
                        restTemplate.postForObject(
                                url,
                                request,
                                Map.class
                        );

                if (response == null) {
                    return "Gemini returned an empty response.";
                }

                var candidates =
                        (List<Map<String, Object>>)
                                response.get("candidates");

                if (candidates == null ||
                        candidates.isEmpty()) {

                    return "Gemini could not generate an answer.";
                }

                var content =
                        (Map<String, Object>)
                                candidates.get(0)
                                        .get("content");

                var parts =
                        (List<Map<String, Object>>)
                                content.get("parts");

                if (parts == null ||
                        parts.isEmpty()) {

                    return "Gemini returned an empty answer.";
                }

                return (String) parts.get(0).get("text");

            } catch (
                    org.springframework.web.client
                            .HttpServerErrorException.ServiceUnavailable e) {

                if (attempt < 2) {

                    try {
                        Thread.sleep(700L);
                    } catch (InterruptedException ex) {

                        Thread.currentThread().interrupt();

                        return "CampusAI was interrupted. Please try again.";
                    }

                } else {

                    return "🤖 Gemini is currently busy. Please try again.";
                }

            } catch (Exception e) {

                e.printStackTrace();

                return "❌ CampusAI could not process your request.";
            }
        }

        return "❌ CampusAI could not process your request.";
    }
}