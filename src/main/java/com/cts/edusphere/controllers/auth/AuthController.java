package com.cts.edusphere.controllers.auth;

import com.cts.edusphere.common.dto.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        try {

            var role =
                    principal.authorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .filter(auth -> auth.startsWith("role"))
                            .findFirst()
                            .map(auth -> auth.substring(5))
                            .orElse("USER");
            Role roleEnum = Role.valueOf(role);
            String accessToken = jwtService.generateAccessToken(principal.userId().toString(), principal.name(), roleEnum, TokenType.ACCESS);
            String refreshToken = jwtService.generateAccessToken(principal.userId().toString(), principal.name(), roleEnum, TokenType.REFRESH);
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
                            .filter(auth -> auth.startsWith("role"))
                            .findFirst()
                            .map(auth -> auth.substring(5))
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

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(request.userId(), request.currentPassword(), request.newPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Password change failed for user: {}", request.userId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
