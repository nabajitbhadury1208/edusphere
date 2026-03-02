package com.cts.edusphere.common.dto;

import java.util.UUID;

public record ChangePasswordRequest(UUID userId, String currentPassword, String newPassword) {
}
