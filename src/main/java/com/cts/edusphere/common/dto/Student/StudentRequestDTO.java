package com.cts.edusphere.common.dto.Student;

import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record StudentRequestDTO(
        @NotBlank(message = "Name cannot be blank") String name,
        @Email(message = "Email should be valid") @NotBlank(message = "Email cannot be blank") String email,
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits") String phone,
        @NotBlank(message = "Password cannot be blank") String password, //TODO NB check this
        @NotNull(message = "Date of birth cannot be null") @PastOrPresent(message = "Date of birth must be in the past") LocalDate dob,
        @NotNull(message = "Gender cannot be null") Gender gender,
        @NotBlank(message = "Address cannot be blank") String address,
        @NotNull(message = "Status cannot be null") Status status
) {}
