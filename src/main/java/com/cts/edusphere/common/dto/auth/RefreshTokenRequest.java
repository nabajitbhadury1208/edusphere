package com.cts.edusphere.common.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for requesting a new access token using a refresh token.
 * Sent by the client when the existing access token has expired.
 *
 * @param refreshToken the valid, non-expired refresh token previously issued during authentication
 */
public record RefreshTokenRequest(
        @NotBlank(message = "Refresh token must not be blank")
        String refreshToken
) {
}
