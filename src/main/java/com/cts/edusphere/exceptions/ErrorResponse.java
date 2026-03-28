package com.cts.edusphere.exceptions;

import lombok.*;

import java.time.Instant;
import java.util.Map;

/**
 * Standardised error payload returned by the global exception handler for all
 * failed API requests.
 *
 * <p>Every field is optional so that the same object can be reused for both
 * simple error messages and bean-validation failures that carry per-field
 * details in {@link #validationError}.</p>
 */
@Builder
@Getter
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
