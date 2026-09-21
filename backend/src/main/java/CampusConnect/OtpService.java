package CampusConnect;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, OtpData> otpStorage =
            new ConcurrentHashMap<>();

    private final Random random = new Random();

    public String generateOtp(String email) {

        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        otpStorage.put(
                email.toLowerCase(),
                new OtpData(
                        otp,
                        LocalDateTime.now().plusMinutes(10)
                )
        );

        return otp;
    }

    public boolean verifyOtp(String email, String otp) {

        OtpData data =
                otpStorage.get(email.toLowerCase());

        if (data == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(data.expiry)) {
            otpStorage.remove(email.toLowerCase());
            return false;
        }

        if (!data.otp.equals(otp)) {
            return false;
        }

        otpStorage.remove(email.toLowerCase());

        return true;
    }

    private static class OtpData {

        private final String otp;
        private final LocalDateTime expiry;

        public OtpData(
                String otp,
                LocalDateTime expiry) {

            this.otp = otp;
            this.expiry = expiry;
        }
    }
}