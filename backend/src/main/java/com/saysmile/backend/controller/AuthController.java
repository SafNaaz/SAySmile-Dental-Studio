package com.saysmile.backend.controller;

import com.saysmile.backend.dto.AuthResponse;
import com.saysmile.backend.dto.LoginRequest;
import com.saysmile.backend.dto.SignupRequest;
import com.saysmile.backend.entity.Patient;
import com.saysmile.backend.entity.Role;
import com.saysmile.backend.entity.User;
import com.saysmile.backend.repository.PatientRepository;
import com.saysmile.backend.repository.UserRepository;
import com.saysmile.backend.security.JwtUtil;
import com.saysmile.backend.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final LdapTemplate ldapTemplate;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        System.out.println("Processing login for user: " + username + " (Type: " + request.getUserType() + ")");

        if ("STAFF".equalsIgnoreCase(request.getUserType())) {
            // 1. Try LDAP Authentication first
            try {
                ldapTemplate.authenticate(
                        LdapQueryBuilder.query().where("uid").is(username),
                        password);

                System.out.println("LDAP Authentication successful for: " + username);

                User user = userRepository.findByUsername(username).orElseGet(() -> {
                    System.out.println("User not found in local DB. Provisioning from LDAP: " + username);
                    return ldapTemplate.search(
                            LdapQueryBuilder.query().where("uid").is(username),
                            (org.springframework.ldap.core.AttributesMapper<User>) attrs -> {
                                String roleStr = (String) attrs.get("employeeType").get();
                                String fullName = attrs.get("cn") != null ? (String) attrs.get("cn").get()
                                        : "Staff User";
                                Role role = Role.FRONT_DESK;
                                try {
                                    role = Role.valueOf(roleStr);
                                } catch (Exception e) {
                                    System.err.println(
                                            "Invalid role in LDAP: " + roleStr + ". Defaulting to FRONT_DESK.");
                                }
                                return userRepository.save(User.builder()
                                        .username(username)
                                        .role(role)
                                        .fullName(fullName)
                                        .email(username)
                                        .build());
                            }).get(0);
                });
                return performLogin(user);
            } catch (Exception ldapEx) {
                System.out.println("LDAP Authentication failed for: " + username + " - " + ldapEx.getMessage());
            }

            // 2. Fallback DB auth
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getPassword() != null &&
                    userOpt.get().getPassword().equals(password) &&
                    userOpt.get().getRole() != Role.PATIENT) {
                return performLogin(userOpt.get());
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } else {
            // Patient login
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent() && userOpt.get().getPassword() != null &&
                    userOpt.get().getPassword().equals(password) &&
                    userOpt.get().getRole() == Role.PATIENT) {
                return performLogin(userOpt.get());
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username (phone) already exists.");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // should be hashed in production
                .fullName(request.getFullName())
                .phone(request.getUsername())
                .role(Role.PATIENT)
                .build();
        newUser = userRepository.save(newUser);

        Patient patient = Patient.builder()
                .user(newUser)
                .build();
        patientRepository.save(patient);

        return performLogin(newUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginRequest request) {
        tokenService.blacklistToken(request.getUsername());
        return ResponseEntity.ok("Logged out successfully");
    }

    private ResponseEntity<AuthResponse> performLogin(User user) {
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        tokenService.storeToken(token, user.getUsername());
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .fullName(user.getFullName())
                .build());
    }
}
