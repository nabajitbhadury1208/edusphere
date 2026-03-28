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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for all JSON Web Token (JWT) operations within the EduSphere application.
 *
 * <p>This service handles the full JWT lifecycle, including:
 * <ul>
 *   <li>Generating signed access tokens and refresh tokens for authenticated users.</li>
 *   <li>Parsing and cryptographically validating incoming tokens.</li>
 *   <li>Extracting a {@link UserPrincipal} from a validated token's claims.</li>
 * </ul>
 *
 * <p>Token signing uses an HMAC-SHA key derived from the Base64-encoded secret configured
 * via the {@code jwt.secret} application property. Expiration windows are controlled by
 * {@code jwt.expiration-in-minutes} (access tokens, default 15 minutes) and
 * {@code jwt.refresh-expiration-in-days} (refresh tokens, default 30 days).
 */
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expiration-in-minutes:15}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration-in-days:30}")
    private long refreshTokenExpiration;

    /**
     * Builds and returns the HMAC-SHA {@link SecretKey} used to sign and verify JWTs.
     *
     * <p>The key is derived from the Base64-decoded value of the {@code jwt.secret}
     * application property.
     *
     * @return a {@link SecretKey} suitable for use with the JJWT library
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    /**
     * Generates a signed JWT — either an access token or a refresh token — for the given user.
     *
     * <p>The token payload contains the following claims:
     * <ul>
     *   <li>{@code sub} / {@code userId} — the user's unique identifier</li>
     *   <li>{@code name} — the user's display name</li>
     *   <li>{@code type} — {@code "access"} or {@code "refresh"}, derived from {@code type}</li>
     *   <li>{@code roles} — a list of role name strings (e.g. {@code ["ADMIN", "USER"]})</li>
     * </ul>
     *
     * <p>Expiration is calculated as:
     * <ul>
     *   <li>Access token: {@code jwt.expiration-in-minutes} converted to milliseconds</li>
     *   <li>Refresh token: {@code jwt.refresh-expiration-in-days} converted to milliseconds</li>
     * </ul>
     *
     * @param userId the unique identifier of the user (stored as both the JWT subject and a claim)
     * @param name   the display name of the user, embedded in the token claims
     * @param roles  the set of {@link Role} values assigned to the user
     * @param type   the {@link TokenType} indicating whether to produce an access or refresh token
     * @return a compact, URL-safe, signed JWT string
     */
    public String generateAccessToken(String userId, String name, Set<Role> roles, TokenType type) {

        String typeClaim = switch (type) {
            case ACCESS -> "access";
            case REFRESH -> "refresh";
        };
        long expiration = switch (type) {
            case ACCESS -> jwtExpiration * 60 * 1000;
            case REFRESH -> refreshTokenExpiration * 24 * 60 * 60 * 1000;
        };
        return Jwts.builder()
                .subject(userId)
                .claim("userId", userId)
                .claim("name", name)
                .claim("type", typeClaim)
                .claim("roles", roles.stream().map(Enum::name).collect(Collectors.toList()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Parses the given JWT string and returns its validated {@link Claims} payload.
     *
     * <p>This method verifies the token's signature using the application signing key and
     * checks that the token has not expired. Any failure results in an
     * {@link InvalidTokenException} with a descriptive message.
     *
     * @param token the compact JWT string to parse and validate
     * @return the {@link Claims} payload extracted from the validated token
     * @throws InvalidTokenException if the token is expired, has an invalid signature,
     *                               is malformed, or is otherwise rejected by the JJWT parser
     */
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
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw new InvalidTokenException("Invalid JWT token");
        }
    }

    /**
     * Constructs a {@link UserPrincipal} from the given JWT {@link Claims}.
     *
     * <p>Extracts the user ID (from the {@code sub} claim), display name, and role list,
     * then maps each role string to a prefixed {@link SimpleGrantedAuthority} (e.g.
     * {@code "ROLE_ADMIN"}). Throws if the {@code roles} claim is absent or empty.
     *
     * @param claims the validated claims payload from which to build the principal
     * @return a {@link UserPrincipal} populated with the user's identity and authorities
     * @throws InvalidTokenException if the {@code roles} claim is missing or empty
     */
    private UserPrincipal buildUserPrincipal(Claims claims) {
        String userId = claims.getSubject();

        String name = claims.get("name", String.class);
        List<String> roleStr = claims.get("roles", List.class);
        if (roleStr == null || roleStr.isEmpty()) {
            throw new InvalidTokenException("Missing role claim in token");
        }
        List<SimpleGrantedAuthority> authorities = roleStr.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        return new UserPrincipal(UUID.fromString(userId), name, authorities);
    }

    /**
     * Validates the given JWT string as an access token and returns the authenticated principal.
     *
     * <p>The token is first parsed and signature-verified via {@link #parseAndValidateClaims(String)}.
     * The {@code type} claim is then checked to confirm the value is {@code "access"}.
     * A {@link UserPrincipal} is built from the remaining claims.
     *
     * @param token the compact JWT string expected to be an access token
     * @return a {@link UserPrincipal} representing the authenticated user
     * @throws InvalidTokenException if the token is invalid, expired, or its {@code type}
     *                               claim is not {@code "access"}
     */
    public UserPrincipal getUserPrincipalFromToken(String token) {
        Claims claims = parseAndValidateClaims(token);
        String type = claims.get("type", String.class);
        if (!"access".equals(type)) {
            throw new InvalidTokenException("Invalid token type: expected access token");
        }
        return buildUserPrincipal(claims);
    }

    /**
     * Validates the given JWT string as a refresh token and returns the authenticated principal.
     *
     * <p>The token is first parsed and signature-verified via {@link #parseAndValidateClaims(String)}.
     * The {@code type} claim is then checked to confirm the value is {@code "refresh"}.
     * A {@link UserPrincipal} is built from the remaining claims.
     *
     * @param token the compact JWT string expected to be a refresh token
     * @return a {@link UserPrincipal} representing the authenticated user
     * @throws InvalidTokenException if the token is invalid, expired, or its {@code type}
     *                               claim is not {@code "refresh"}
     */
    public UserPrincipal getUserPrincipalFromRefreshToken(String token) {
        Claims claims = parseAndValidateClaims(token);
        String type = claims.get("type", String.class);
        if (!"refresh".equals(type)) {
            throw new InvalidTokenException("Invalid token type: expected refresh token");
        }
        return buildUserPrincipal(claims);
    }

}
