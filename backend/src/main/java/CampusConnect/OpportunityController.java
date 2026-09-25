package CampusConnect;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin(origins = { "https://campusconnect-web-me6v.onrender.com", "https://campusconnect-dva.pages.dev" })
public class OpportunityController {

    private final OpportunityRepository opportunityRepository;
    private final UserRepository userRepository;

    public OpportunityController(
            OpportunityRepository opportunityRepository,
            UserRepository userRepository) {

        this.opportunityRepository = opportunityRepository;
        this.userRepository = userRepository;
    }


    // ===============================
    // GET PUBLISHED OPPORTUNITIES
    // ===============================

    @GetMapping("/api/opportunities")
    public ResponseEntity<?> getPublishedOpportunities() {

        List<Opportunity> opportunities =
                opportunityRepository
                        .findByStatusOrderByCreatedAtDesc(
                                "PUBLISHED"
                        );

        List<OpportunityResponse> response =
                opportunities.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    // ===============================
    // ADMIN - GET ALL OPPORTUNITIES
    // ===============================

    @GetMapping("/api/admin/opportunities")
    public ResponseEntity<?> getAllOpportunities(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader) {

        User admin =
                getAdmin(authorizationHeader);

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        List<Opportunity> opportunities =
                opportunityRepository.findAll();

        List<OpportunityResponse> response =
                opportunities.stream()
                        .map(this::convertToResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    // ===============================
    // ADMIN - ADD OPPORTUNITY
    // ===============================

    @PostMapping("/api/admin/opportunities")
    public ResponseEntity<?> addOpportunity(
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader,
            @RequestBody Opportunity opportunity) {

        User admin =
                getAdmin(authorizationHeader);

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        opportunity.setStatus("PUBLISHED");

        opportunity.setCreatedAt(
                LocalDateTime.now()
        );

        Opportunity saved =
                opportunityRepository.save(
                        opportunity
                );

        return ResponseEntity.ok(
                convertToResponse(saved)
        );
    }


    // ===============================
    // ADMIN - UPDATE OPPORTUNITY
    // ===============================

    @PutMapping("/api/admin/opportunities/{id}")
    public ResponseEntity<?> updateOpportunity(
            @PathVariable Long id,
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader,
            @RequestBody Opportunity updated) {

        User admin =
                getAdmin(authorizationHeader);

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        Opportunity opportunity =
                opportunityRepository
                        .findById(id)
                        .orElse(null);

        if (opportunity == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Opportunity not found");
        }

        opportunity.setCompany(
                updated.getCompany()
        );

        opportunity.setTitle(
                updated.getTitle()
        );

        opportunity.setType(
                updated.getType()
        );

        opportunity.setRole(
                updated.getRole()
        );

        opportunity.setLocation(
                updated.getLocation()
        );

        opportunity.setWorkMode(
                updated.getWorkMode()
        );

        opportunity.setEligibility(
                updated.getEligibility()
        );

        opportunity.setCgpa(
                updated.getCgpa()
        );

        opportunity.setSkills(
                updated.getSkills()
        );

        opportunity.setStipend(
                updated.getStipend()
        );

        opportunity.setSalary(
                updated.getSalary()
        );

        opportunity.setDeadline(
                updated.getDeadline()
        );

        opportunity.setApplyUrl(
                updated.getApplyUrl()
        );

        opportunity.setDescription(
                updated.getDescription()
        );

        opportunity.setCompanyLogo(
                updated.getCompanyLogo()
        );

        if (updated.getStatus() != null) {

            opportunity.setStatus(
                    updated.getStatus()
            );
        }

        Opportunity saved =
                opportunityRepository.save(
                        opportunity
                );

        return ResponseEntity.ok(
                convertToResponse(saved)
        );
    }


    // ===============================
    // ADMIN - DELETE OPPORTUNITY
    // ===============================

    @DeleteMapping("/api/admin/opportunities/{id}")
    public ResponseEntity<?> deleteOpportunity(
            @PathVariable Long id,
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader) {

        User admin =
                getAdmin(authorizationHeader);

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        Opportunity opportunity =
                opportunityRepository
                        .findById(id)
                        .orElse(null);

        if (opportunity == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Opportunity not found");
        }

        opportunityRepository.delete(
                opportunity
        );

        return ResponseEntity.ok(
                "Opportunity deleted successfully"
        );
    }


    // ===============================
    // ADMIN - PUBLISH / HIDE
    // ===============================

    @PutMapping(
            "/api/admin/opportunities/{id}/status"
    )
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestHeader(
                    value = "Authorization",
                    required = false
            )
            String authorizationHeader) {

        User admin =
                getAdmin(authorizationHeader);

        if (admin == null) {

            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Admin access required");
        }

        if (!status.equals("PUBLISHED") &&
                !status.equals("HIDDEN")) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid status");
        }

        Opportunity opportunity =
                opportunityRepository
                        .findById(id)
                        .orElse(null);

        if (opportunity == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Opportunity not found");
        }

        opportunity.setStatus(status);

        Opportunity saved =
                opportunityRepository.save(
                        opportunity
                );

        return ResponseEntity.ok(
                convertToResponse(saved)
        );
    }


    // ===============================
    // ADMIN CHECK
    // ===============================

    private User getAdmin(
            String authorizationHeader) {

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith(
                        "Bearer "
                )) {

            return null;
        }

        String token =
                authorizationHeader.substring(7);

        User user =
                userRepository.findByToken(token);

        if (user == null ||
                !"ADMIN".equals(user.getRole())) {

            return null;
        }

        return user;
    }


    // ===============================
    // RESPONSE CONVERTER
    // ===============================

    private OpportunityResponse convertToResponse(
            Opportunity opportunity) {

        return new OpportunityResponse(

                opportunity.getId(),

                opportunity.getCompany(),

                opportunity.getTitle(),

                opportunity.getType(),

                opportunity.getRole(),

                opportunity.getLocation(),

                opportunity.getWorkMode(),

                opportunity.getEligibility(),

                opportunity.getCgpa(),

                opportunity.getSkills(),

                opportunity.getStipend(),

                opportunity.getSalary(),

                opportunity.getDeadline(),

                opportunity.getApplyUrl(),

                opportunity.getDescription(),

                opportunity.getCompanyLogo(),

                opportunity.getStatus(),

                opportunity.getCreatedAt()
        );
    }
}