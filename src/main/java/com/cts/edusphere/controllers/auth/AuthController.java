package com.cts.edusphere.controllers.auth;

import com.cts.edusphere.common.dto.auth.*;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.config.security.TokenType;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), user.getRole(),
                    TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(),
                    user.getRole(), TokenType.REFRESH);

            log.info("User registered successfully: {}", user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            // FIXED: was .build() (empty body) — now returns a meaningful error message
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Registration failed", "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            var authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationManager.authenticate(authRequest);

            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .findFirst()
                    .map(auth -> auth.substring(5))
                    .orElse("USER");
            Role roleEnum = Role.valueOf(role);

            User user = userService.getUserByEmail(request.email());
            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), roleEnum,
                    TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), roleEnum,
                    TokenType.REFRESH);
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            // FIXED: was .build() (empty body) — now returns a meaningful error message
            log.error("Login failed for user: {}", request.email(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid email or password"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            UserPrincipal principal = jwtService.getUserPrincipalFromRefreshToken(request.refreshToken());
            User user = userService.getUserById(principal.userId());

            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), user.getRole(),
                    TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(),
                    user.getRole(), TokenType.REFRESH);

            log.info("Token refreshed successfully for user: {}", user.getId());
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            // FIXED: was .build() (empty body) — now returns a meaningful error message
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid or expired refresh token"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            log.error("Unauthorized attempt to logout");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful (client should discard tokens)");
        log.info("User logged out: {}", principal.userId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        userService.changePassword(principal.userId(), request.currentPassword(), request.newPassword());
        log.info("Password changed successfully for user: {}", principal.userId());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

}
