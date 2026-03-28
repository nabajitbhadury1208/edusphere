package com.cts.edusphere.config.security;

import com.cts.edusphere.exceptions.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

/**
 * Central Spring Security configuration for the EduSphere application.
 *
 * <p>This class wires together all security infrastructure beans and defines the
 * application's HTTP security policy. Key responsibilities include:
 * <ul>
 *   <li>Registering the {@link JwtAuthenticationFilter} in the filter chain so that
 *       every request is inspected for a valid Bearer token before reaching controllers.</li>
 *   <li>Configuring a stateless session policy — no HTTP session is created or used.</li>
 *   <li>Disabling CSRF protection, which is unnecessary for stateless REST APIs.</li>
 *   <li>Defining CORS rules driven by the {@code cors.allowed-origins} property
 *       (defaults to {@code http://localhost:3000}).</li>
 *   <li>Exposing public endpoints (login, register, token refresh, Swagger UI) while
 *       requiring authentication for all other requests.</li>
 *   <li>Returning structured JSON {@link ErrorResponse} bodies for both authentication
 *       failures ({@code 401}) and access-denied events ({@code 403}).</li>
 * </ul>
 *
 * <p>Method-level security is enabled via {@link EnableMethodSecurity}, allowing
 * {@code @PreAuthorize} and related annotations throughout the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    @Value("${cors.allowed-origins:http://localhost:3000}")
    private List<String> allowedOrigins;

    /**
     * Constructs a new {@code SecurityConfig} with the required collaborators.
     *
     * @param jwtAuthenticationFilter the filter that validates JWT Bearer tokens on each request
     * @param userDetailsService      the service used to load user details during authentication
     * @param objectMapper            the Jackson {@link ObjectMapper} used to serialize JSON error responses
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    /**
     * Exposes the application's {@link AuthenticationManager} as a Spring bean.
     *
     * <p>The manager is obtained from the auto-configured {@link AuthenticationConfiguration}
     * and can be injected into services that need to programmatically trigger authentication
     * (e.g. the login endpoint).
     *
     * @param config Spring's {@link AuthenticationConfiguration} from which the manager is retrieved
     * @return the configured {@link AuthenticationManager}
     * @throws Exception if the authentication manager cannot be resolved
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Produces a {@link DaoAuthenticationProvider} that authenticates users against the
     * database via {@link CustomUserDetailsService} and verifies passwords with BCrypt.
     *
     * @return a fully configured {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Provides a {@link BCryptPasswordEncoder} bean used to hash and verify user passwords.
     *
     * @return a {@link PasswordEncoder} backed by the BCrypt algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines the application's CORS policy as a {@link CorsConfigurationSource} bean.
     *
     * <p>The configuration allows:
     * <ul>
     *   <li>Origins listed in the {@code cors.allowed-origins} property (default:
     *       {@code http://localhost:3000})</li>
     *   <li>HTTP methods: {@code GET, POST, PUT, DELETE, OPTIONS, PATCH}</li>
     *   <li>Headers: {@code Authorization, Accept, Content-Type}</li>
     *   <li>Credentials (cookies / Authorization headers) to be included in cross-origin requests</li>
     *   <li>Pre-flight cache duration of 3600 seconds (1 hour)</li>
     * </ul>
     *
     * <p>The policy is applied globally to all URL patterns ({@code /**}).
     *
     * @return a {@link CorsConfigurationSource} that applies the above rules to every endpoint
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Accept", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    /**
     * Builds and returns the application's primary {@link SecurityFilterChain}.
     *
     * <p>The chain is configured with the following rules:
     * <ul>
     *   <li>CSRF is disabled — appropriate for a stateless JWT-secured REST API.</li>
     *   <li>CORS is configured via {@link #corsConfigurationSource()}.</li>
     *   <li>Session management is set to {@link SessionCreationPolicy#STATELESS} so no
     *       server-side HTTP session is created or consulted.</li>
     *   <li>The {@link #authenticationProvider()} is registered for credential validation.</li>
     *   <li>A custom {@code authenticationEntryPoint} returns a JSON {@code 401} body when
     *       an unauthenticated request reaches a protected resource.</li>
     *   <li>A custom {@code accessDeniedHandler} returns a JSON {@code 403} body when an
     *       authenticated user lacks the required authority.</li>
     *   <li>The following paths are publicly accessible without authentication:
     *       {@code /api/v1/auth/login}, {@code /api/v1/auth/register},
     *       {@code /api/v1/auth/refresh}, {@code /v3/api-docs/**},
     *       {@code /swagger-ui/**}, and {@code /swagger-ui.html}.</li>
     *   <li>All other requests require a valid, authenticated session.</li>
     *   <li>{@link JwtAuthenticationFilter} is inserted before the default
     *       {@link UsernamePasswordAuthenticationFilter}.</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} builder provided by Spring Security
     * @return the fully built {@link SecurityFilterChain}
     * @throws Exception if any step of the {@link HttpSecurity} configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider())

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            ErrorResponse body = ErrorResponse.builder()
                                    .timeStamp(Instant.now())
                                    .status(HttpStatus.UNAUTHORIZED.value())
                                    .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                    .message("Authentication required: " + authException.getMessage())
                                    .path(request.getRequestURI())
                                    .build();
                            response.getWriter().write(objectMapper.writeValueAsString(body));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            ErrorResponse body = ErrorResponse.builder()
                                    .timeStamp(Instant.now())
                                    .status(HttpStatus.FORBIDDEN.value())
                                    .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                                    .message("Access denied: " + accessDeniedException.getMessage())
                                    .path(request.getRequestURI())
                                    .build();

                            response.getWriter().write(objectMapper.writeValueAsString(body));
                        }))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/auth/register",
                                "/api/v1/auth/refresh",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .anyRequest().authenticated())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
