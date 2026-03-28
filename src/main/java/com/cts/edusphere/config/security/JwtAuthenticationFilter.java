package com.cts.edusphere.config.security;

import com.cts.edusphere.exceptions.ErrorResponse;
import com.cts.edusphere.exceptions.genericexceptions.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

/**
 * Servlet filter that intercepts every HTTP request exactly once and enforces JWT-based
 * authentication for the EduSphere application.
 *
 * <p>The filter operates as follows:
 * <ol>
 *   <li>Reads the {@code Authorization} request header. If the header is absent or does not
 *       begin with {@code "Bearer "}, the request is passed through without modification.</li>
 *   <li>Extracts the JWT string and delegates validation to {@link JwtService}.</li>
 *   <li>On success, populates the {@link SecurityContextHolder} with a fully authenticated
 *       {@link UsernamePasswordAuthenticationToken} backed by the resolved {@link UserPrincipal}.</li>
 *   <li>On failure ({@link InvalidTokenException} or any other exception), clears the security
 *       context and writes a structured JSON {@code 401 Unauthorized} error response using
 *       {@link ErrorResponse}, halting further filter chain execution.</li>
 * </ol>
 *
 * <p>Extends {@link OncePerRequestFilter} to guarantee single execution per request,
 * regardless of the servlet container's dispatch mechanism.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new {@code JwtAuthenticationFilter} with the required dependencies.
     *
     * @param jwtService   the service used to validate JWT strings and extract principals
     * @param objectMapper the Jackson {@link ObjectMapper} used to serialize error responses to JSON
     */
    public JwtAuthenticationFilter(JwtService jwtService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    /**
     * Core filter logic executed once per request.
     *
     * <p>Reads the {@code Authorization} header and, when a {@code Bearer} token is present,
     * attempts to validate it and set up the Spring Security authentication context.
     * If no token is present, the filter chain proceeds normally without authentication.
     * If the token is present but invalid or expired, the filter short-circuits the chain
     * and writes a JSON {@code 401 Unauthorized} error directly to the response.
     *
     * @param request     the incoming HTTP servlet request
     * @param response    the outgoing HTTP servlet response
     * @param filterChain the remaining filter chain to invoke if authentication succeeds or
     *                    if no token is present in the request
     * @throws ServletException if a servlet-level error occurs during filter processing
     * @throws IOException      if an I/O error occurs while writing the error response
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserPrincipal userPrincipal = jwtService.getUserPrincipalFromToken(jwtToken);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userPrincipal, null, userPrincipal.authorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (InvalidTokenException e) {
            SecurityContextHolder.clearContext();
            writeJsonErrorResponse(response,HttpStatus.UNAUTHORIZED, "Invalid token: " + e.getMessage(), request.getRequestURI());
            return;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            writeJsonErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized: " + e.getMessage(), request.getRequestURI());
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Writes a structured JSON error response to the HTTP response.
     *
     * <p>Sets the HTTP status code and {@code Content-Type} header to
     * {@code application/json}, then serializes an {@link ErrorResponse} containing
     * the current timestamp, status code, reason phrase, descriptive message, and
     * request path.
     *
     * @param response the {@link HttpServletResponse} to which the error body is written
     * @param status   the {@link HttpStatus} to apply to the response (e.g. {@code 401 Unauthorized})
     * @param message  a human-readable description of the error
     * @param path     the request URI that triggered the error, included in the response body
     * @throws IOException if an I/O error occurs while writing to the response output stream
     */
    private void writeJsonErrorResponse(HttpServletResponse response, HttpStatus status, String message, String path) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = ErrorResponse.builder()
                .timeStamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .build();
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
