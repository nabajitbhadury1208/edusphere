package com.cts.edusphere.common.dto;


public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
