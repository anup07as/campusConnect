package CampusConnect;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.UUID;
@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    // REGISTER
    @PostMapping("/api/auth/register")
    public ResponseEntity<?> register(
            @RequestBody User user) {
        User existingUser =
                userRepository.findByEmail(
                        user.getEmail()
                );
        if (existingUser != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }
        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );
        user.setRole("STUDENT");
        return ResponseEntity.ok(
                userRepository.save(user)
        );
    }
    // LOGIN
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> login(
            @RequestBody User loginUser) {
        User user =
                userRepository.findByEmail(
                        loginUser.getEmail()
                );
        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
        if (!passwordEncoder.matches(
                loginUser.getPassword(),
                user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password");
        }
        // Generate a new token
        String token =
                UUID.randomUUID().toString();
        user.setToken(token);
        userRepository.save(user);
        // Send only safe information to browser
        LoginResponse response =
                new LoginResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getToken()
                );
        return ResponseEntity.ok(response);
    }
}