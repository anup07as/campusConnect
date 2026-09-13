package CampusConnect;
import org.springframework.stereotype.Service;
@Service
public class AdminAuthService {
    private final UserRepository userRepository;
    public AdminAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public boolean isAdmin(String authorizationHeader) {
        if (authorizationHeader == null) {
            return false;
        }
        if (!authorizationHeader.startsWith("Bearer ")) {
            return false;
        }
        String token =
                authorizationHeader.substring(7);
        User user =
                userRepository.findByToken(token);
        if (user == null) {
            return false;
        }
        return "ADMIN".equals(user.getRole());
    }
}