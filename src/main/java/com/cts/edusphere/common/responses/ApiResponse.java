package com.cts.edusphere.common.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private String message;
    private boolean success;
    private T data;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}