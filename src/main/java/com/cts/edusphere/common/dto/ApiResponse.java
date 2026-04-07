package com.cts.edusphere.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(
        String message,
        Integer status,
        Instant timeStamp
) {
    public static ApiResponse of(String message, Integer status) {
        return ApiResponse.builder()
                .message(message)
                .status(status)
                .timeStamp(Instant.now())
                .build();
    }
}

