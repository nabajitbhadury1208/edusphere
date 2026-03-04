package com.cts.edusphere.config.security;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.exceptions.genericexceptions.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
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

        String typeClaim = switch (type) {
            case ACCESS -> "access";
            case REFRESH -> "refresh";
        };
        long expiration = switch (type) {
            case ACCESS -> jwtExpiration * 60 * 1000; // Convert minutes to milliseconds
            case REFRESH -> refreshTokenExpiration * 24 * 60 * 60 * 1000; // Convert days to milliseconds
        };
        return Jwts.builder()
                .subject(userId)
                .claim("userId", userId)
                .claim("name", name)
                .claim("type", typeClaim)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims parseAndValidateClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("JWT token has expired: {}", e.getMessage());
            throw new InvalidTokenException("JWT token has expired");
        }
        catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw new InvalidTokenException("Invalid JWT token");
        }
    }

    private UserPrincipal buildUserPrincipal(Claims claims){
        String userId = claims.getSubject();
        String name = claims.get("name", String.class);
        String role = claims.get("role", String.class);
        if(role == null){
            throw new InvalidTokenException("Missing role claim in token");
        }
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
        return new UserPrincipal(UUID.fromString(userId), name, authorities);
    }

    public UserPrincipal getUserPrincipalFromToken(String token) {
        Claims claims = parseAndValidateClaims(token);
        String type = claims.get("type", String.class);
        if (!"access".equals(type)) {
            throw new InvalidTokenException("Invalid token type: expected access token");
        }
        return buildUserPrincipal(claims);
    }

    public UserPrincipal getUserPrincipalFromRefreshToken(String token) {
        Claims claims = parseAndValidateClaims(token);
        String type = claims.get("type", String.class);
        if (!"refresh".equals(type)) {
            throw new InvalidTokenException("Invalid token type: expected refresh token");
        }
        return buildUserPrincipal(claims);
    }

}
