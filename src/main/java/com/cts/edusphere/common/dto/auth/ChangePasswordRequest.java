package com.cts.edusphere.common.dto.auth;


public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
