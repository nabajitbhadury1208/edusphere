package com.cts.edusphere.common.dto.auth;

/**
 * Data Transfer Object returned upon successful authentication.
 * Contains the JWT tokens required for accessing protected API endpoints.
 *
 * @param accessToken  the short-lived JWT used to authenticate subsequent API requests
 * @param refreshToken the long-lived token used to obtain a new access token when it expires
 */
public record AuthResponse(String accessToken, String refreshToken) {
}
