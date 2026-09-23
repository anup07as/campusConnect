package CampusConnect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtp(String email, String otp) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", "CampusConnect",
                        "email", senderEmail
                ),
                "to", new Object[]{
                        Map.of(
                                "email", email
                        )
                },
                "subject", "CampusConnect Email Verification",
                "textContent",
                "Your CampusConnect verification OTP is: "
                        + otp
                        + "\n\nThis OTP is valid for 10 minutes."
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForObject(
                url,
                request,
                String.class
        );
    }
}