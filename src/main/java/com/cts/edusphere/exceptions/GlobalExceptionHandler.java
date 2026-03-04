package com.cts.edusphere.exceptions;

import com.cts.edusphere.common.responses.ApiResponse;
import com.cts.edusphere.exceptions.genericexceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFound(ResourceNotFoundException ex) {
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message(ex.getMessage())
                .success(false)
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}