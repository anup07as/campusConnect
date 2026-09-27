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

if (user.getTokenCreatedAt() == null ||
        user.getTokenCreatedAt()
            .plusHours(24)
            .isBefore(java.time.LocalDateTime.now())) {

    user.setToken(null);
    user.setTokenCreatedAt(null);
    userRepository.save(user);

    return false;
}

return "ADMIN".equals(user.getRole());
    }
}