package com.cts.edusphere.controllers.auth;

import com.cts.edusphere.common.dto.auth.*;
import com.cts.edusphere.config.security.JwtService;
import com.cts.edusphere.config.security.TokenType;
import com.cts.edusphere.config.security.UserPrincipal;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.modules.User;
import com.cts.edusphere.services.user.UserService;
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
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), user.getRole(), TokenType.ACCESS);

            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), user.getRole(), TokenType.REFRESH);

            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            var authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            Authentication authentication = authenticationManager.authenticate(authRequest);

            User user = userService.getUserByEmail(request.email());

            var role =
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(auth -> auth.startsWith("ROLE_"))
                            .findFirst()
                            .map(auth -> auth.substring(5))
                            .orElse("USER");
            Role roleEnum = Role.valueOf(role);
            String accessToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), roleEnum, TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(user.getId().toString(), user.getName(), roleEnum, TokenType.REFRESH);
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            log.error("Login failed for user: {}", request.email(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            UserPrincipal principal = jwtService.getUserPrincipalFromToken(request.refreshToken(), TokenType.REFRESH);
            var role =
                    principal.authorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(auth -> auth.startsWith("ROLE_"))
                            .findFirst()
                            .map(auth -> auth.toUpperCase().substring(5))
                            .orElse("USER");
            Role roleEnum = Role.valueOf(role);
            String accessToken = jwtService.generateAccessToken(principal.userId().toString(), principal.name(), roleEnum, TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(principal.userId().toString(), principal.name(), roleEnum, TokenType.REFRESH);
            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            log.error("Unauthorized attempt to change password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            userService.changePassword(principal.userId(), request.currentPassword(), request.newPassword());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password changed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Password change failed for user: {}", principal.userId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
