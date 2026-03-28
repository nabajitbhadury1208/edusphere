package com.cts.edusphere.config.security;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;

/**
 * Immutable value object representing the authenticated user within the Spring Security context.
 *
 * <p>An instance of {@code UserPrincipal} is stored as the principal inside a
 * {@link org.springframework.security.authentication.UsernamePasswordAuthenticationToken}
 * after a JWT has been successfully validated by {@link JwtAuthenticationFilter}. It carries
 * the minimum identity and authorisation information needed by downstream components
 * (controllers, services, method-security expressions) without requiring an additional
 * database lookup per request.
 *
 * <p>Being a Java {@code record}, all fields are final and exposed via their canonical
 * accessor methods ({@link #userId()}, {@link #name()}, {@link #authorities()}).
 *
 * @param userId      the unique identifier of the authenticated user, sourced from the JWT subject claim
 * @param name        the display name of the authenticated user, sourced from the JWT {@code name} claim
 * @param authorities the collection of {@link GrantedAuthority} instances representing the user's
 *                    roles, each prefixed with {@code "ROLE_"} (e.g. {@code "ROLE_ADMIN"})
 */
public record UserPrincipal(UUID userId, String name, Collection<? extends GrantedAuthority> authorities) {

}
