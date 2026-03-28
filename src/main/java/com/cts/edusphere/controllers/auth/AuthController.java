package com.cts.edusphere.controllers.auth;

import com.cts.edusphere.common.dto.auth.*;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.config.security.TokenType;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.modules.user.User;
import com.cts.edusphere.services.notification.NotificationService;
import com.cts.edusphere.services.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
/**
 * REST controller handling user authentication, registration, and token management.
 * Base path: /api/v1/auth
 * The register, login, and refresh endpoints are publicly accessible (no auth required).
 */
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserServiceImpl userService;
    private final NotificationService notificationService;

    /**
     * Registers a new user account in the system.
     * Creates a User entity, encodes the password, assigns the provided roles,
     * generates both access and refresh JWT tokens, subscribes the user to notifications,
     * and returns the token pair.
     *
     * @param request the registration request containing name, email, phone, password, and roles
     * @return HTTP 201 with an AuthResponse (accessToken, refreshToken) on success,
     *         or HTTP 400 with an error message if registration fails
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), user.getRoles(),
                    TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(),
                    user.getRoles(), TokenType.REFRESH);

            log.info("User registered successfully: {}", user.getId());

            notificationService.subscribeToNofications(user.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Registration failed", "message", e.getMessage()));
        }
    }

    /**
     * Authenticates an existing user with their email and password.
     * Validates credentials via Spring Security AuthenticationManager, extracts roles,
     * generates JWT tokens, subscribes the user to notifications, and returns the token pair.
     * Throws DisabledException (HTTP 403) if the user account is inactive.
     *
     * @param request the login request containing email and password
     * @return HTTP 200 with an AuthResponse (accessToken, refreshToken) on success,
     *         HTTP 403 if the account is disabled,
     *         or HTTP 401 with an error message if credentials are invalid
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            var authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationManager.authenticate(authRequest);

            Set<Role> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth != null && auth.startsWith("ROLE_"))
                    .map(auth -> auth.substring(5))
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());
            if (roles.isEmpty()) {
                log.error("No roles found for user: {}", request.email());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Forbidden", "message", "User has no roles assigned"));
            }
            User user = userService.getUserByEmail(request.email());
            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), roles,
                    TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), roles,
                    TokenType.REFRESH);

            notificationService.subscribeToNofications(user.getId());
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (DisabledException e) {
            throw e; // let GenericExceptionHandler return 403
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.email(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid email or password"));
        }
    }

    /**
     * Issues a new access/refresh token pair using a valid refresh token.
     * Validates the provided refresh token, loads the associated user, and generates fresh tokens.
     *
     * @param request the refresh request containing the refresh token string
     * @return HTTP 200 with a new AuthResponse (accessToken, refreshToken) on success,
     *         or HTTP 401 with an error message if the refresh token is invalid or expired
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            UserPrincipal principal = jwtService.getUserPrincipalFromRefreshToken(request.refreshToken());
            User user = userService.getUserById(principal.userId());

            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), user.getRoles(),
                    TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(),
                    user.getRoles(), TokenType.REFRESH);

            log.info("Token refreshed successfully for user: {}", user.getId());
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid or expired refresh token"));
        }
    }

    /**
     * Logs out the currently authenticated user.
     * Since JWT is stateless, logout is client-side: the server simply returns a message
     * advising the client to discard the tokens. No server-side token invalidation occurs.
     *
     * @param principal the authenticated user principal injected by Spring Security
     * @return HTTP 200 with a logout confirmation message,
     *         or HTTP 401 if the user is not authenticated
     */
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

    /**
     * Changes the password of the currently authenticated user.
     * Verifies the current password before updating to the new encoded password.
     *
     * @param request   the change password request containing currentPassword and newPassword
     * @param principal the authenticated user principal providing the userId
     * @return HTTP 200 with a success confirmation message
     */
    @PatchMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                                              @AuthenticationPrincipal UserPrincipal principal) {
        userService.changePassword(principal.userId(), request.currentPassword(), request.newPassword());
        log.info("Password changed successfully for user: {}", principal.userId());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

}
