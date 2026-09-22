package CampusConnect;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.List;

@RestController
@CrossOrigin(origins = {
    "https://campusconnect-web-me6v.onrender.com",
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;

    public UserController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.emailService = emailService;
    }

    // SEND OTP
    @PostMapping("/api/auth/send-otp")
    public ResponseEntity<?> sendOtp(
            @RequestParam String email) {

        User existingUser =
                userRepository.findByEmail(email);

        if (existingUser != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already registered");
        }

        String otp =
                otpService.generateOtp(email);

        try {

            emailService.sendOtp(email, otp);

            return ResponseEntity.ok(
                    "OTP sent successfully"
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to send OTP");
        }
    }

    // VERIFY OTP
    @PostMapping("/api/auth/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        boolean verified =
                otpService.verifyOtp(email, otp);

        if (!verified) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired OTP");
        }

        return ResponseEntity.ok(
                "Email verified successfully"
        );
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

user.setCreatedAt(
        java.time.LocalDateTime.now()
);

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

        String token =
        UUID.randomUUID().toString();

user.setToken(token);

// Set creation date for older accounts
if (user.getCreatedAt() == null) {
    user.setCreatedAt(
            java.time.LocalDateTime.now()
    );
}

userRepository.save(user);

        LoginResponse response =
                new LoginResponse(
                        user.getName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getToken()
                );

        return ResponseEntity.ok(response);
    }
    
// GET CURRENT USER PROFILE
@GetMapping("/api/auth/me")
public ResponseEntity<?> getCurrentUser(
        @RequestHeader(
                value = "Authorization",
                required = false)
        String authorizationHeader) {

    if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Login required");
    }

    String token =
            authorizationHeader.substring(7);

    User user =
            userRepository.findByToken(token);

    if (user == null) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid session");
    }

    return ResponseEntity.ok(
            new UserProfileResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole(),
                    user.getCreatedAt()
            )
    );
}

// CHANGE PASSWORD
@PutMapping("/api/auth/change-password")
public ResponseEntity<?> changePassword(
        @RequestHeader(
                value = "Authorization",
                required = false)
        String authorizationHeader,
        @RequestBody ChangePasswordRequest request) {

    if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Login required");
    }

    String token =
            authorizationHeader.substring(7);

    User user =
            userRepository.findByToken(token);

    if (user == null) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid session");
    }

    // Check current password
    if (!passwordEncoder.matches(
            request.getCurrentPassword(),
            user.getPassword())) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Current password is incorrect");
    }

    // Validate new password
    if (request.getNewPassword() == null ||
            request.getNewPassword().length() < 8) {

        return ResponseEntity
                .badRequest()
                .body("New password must be at least 8 characters");
    }

    // Save new encrypted password
    user.setPassword(
            passwordEncoder.encode(
                    request.getNewPassword()
            )
    );

    userRepository.save(user);

    return ResponseEntity.ok(
            "Password changed successfully"
    );
}

    // GET ALL USERS - ADMIN ONLY
    @GetMapping("/api/admin/users")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader) {

        User admin =
                userRepository.findByToken(
                        authorizationHeader != null
                                && authorizationHeader.startsWith("Bearer ")
                                ? authorizationHeader.substring(7)
                                : null
                );

        if (admin == null ||
                !"ADMIN".equals(admin.getRole())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        List<AdminUserResponse> users =
                userRepository.findAll()
                        .stream()
                        .map(user ->
                                new AdminUserResponse(
                                        user.getId(),
                                        user.getName(),
                                        user.getEmail(),
                                        user.getRole()
                                )
                        )
                        .toList();

        return ResponseEntity.ok(users);
    }

    // DELETE USER - ADMIN ONLY
    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            @RequestHeader(
                    value = "Authorization",
                    required = false)
            String authorizationHeader) {

        User admin =
                userRepository.findByToken(
                        authorizationHeader != null
                                && authorizationHeader.startsWith("Bearer ")
                                ? authorizationHeader.substring(7)
                                : null
                );

        if (admin == null ||
                !"ADMIN".equals(admin.getRole())) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        User user =
                userRepository.findById(id)
                        .orElse(null);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if ("ADMIN".equals(user.getRole())) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin account cannot be deleted");
        }

        userRepository.deleteById(id);

        return ResponseEntity.ok(
                "User deleted successfully"
        );
    }
}