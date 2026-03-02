package com.cts.edusphere.config.security;

import com.cts.edusphere.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expiration-in-minutes:15}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration-in-days:30}")
    private long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public String generateAccessToken(String userId, String name, Role role, TokenType type) {
        var typeClaim = switch (type) {
            case ACCESS -> "access";
            case REFRESH -> "refresh";
        };
        var expiration = switch (type) {
            case ACCESS -> jwtExpiration * 60 * 1000; // Convert minutes to milliseconds
            case REFRESH -> refreshTokenExpiration * 24 * 60 * 60 * 1000; // Convert days to milliseconds
        };
        return Jwts.builder()
                .subject(userId)
                .claim("userId", userId)
                .claim("name", name)
                .claim("type", typeClaim)
                .claim("role", role)
                .issuedAt(new java.util.Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public UserPrincipal getUserPrincipalFromToken(String token, TokenType expectedType) {
        try {
            var expectedTypeClaim = switch (expectedType) {
                case ACCESS -> "access";
                case REFRESH -> "refresh";
            };
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.getSubject();
            String name = claims.get("name", String.class);
            String role = claims.get("role", String.class);
            String type = claims.get("type", String.class);

            if (!expectedTypeClaim.equals(type)) {
                throw new BadCredentialsException("Invalid token type");
            }

            if (role == null) throw new BadCredentialsException("No roles found in token");

            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

            return new UserPrincipal(UUID.fromString(userId), name, authorities);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new BadCredentialsException("Invalid token");
        }
    }

}
