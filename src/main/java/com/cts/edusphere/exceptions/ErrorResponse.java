package com.cts.edusphere.exceptions;

import lombok.*;

import java.time.Instant;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Instant timeStamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationError;
}
