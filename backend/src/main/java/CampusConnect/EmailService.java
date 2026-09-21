package CampusConnect;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void sendOtp(String email, String otp) {
        SimpleMailMessage message =
                new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("CampusConnect Email Verification");
        message.setText(
                "Your CampusConnect verification OTP is: "
                + otp
                + "\n\nThis OTP is valid for 10 minutes."
        );
        mailSender.send(message);
    }
}