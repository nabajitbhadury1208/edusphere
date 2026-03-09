package com.cts.edusphere.common.dto.Student;

import com.cts.edusphere.common.validation.CreateValidation;
import com.cts.edusphere.common.validation.UpdateValidation;
import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Status;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record StudentRequestDTO(
        @NotBlank(message = "Name cannot be blank", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String name,

        @NotBlank(message = "Email cannot be blank", groups = CreateValidation.class)
        @Email(message = "Email should be valid", groups = {CreateValidation.class, UpdateValidation.class})
        String email,

        @NotBlank(message = "Phone number cannot be blank", groups = CreateValidation.class)
        @Pattern(regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits", groups = {CreateValidation.class, UpdateValidation.class})
        String phone,

        @NotBlank(message = "Password cannot be blank", groups = CreateValidation.class)
        @Size(min = 8, message = "Password must be at least 8 characters", groups = CreateValidation.class)
        String password,

        @NotNull(message = "Date of birth cannot be null", groups = CreateValidation.class)
        @PastOrPresent(message = "Date of birth must be in the past", groups = {CreateValidation.class, UpdateValidation.class})
        LocalDate dob,

        @NotNull(message = "Gender cannot be null", groups = CreateValidation.class)
        Gender gender,

        @NotBlank(message = "Address cannot be blank", groups = {CreateValidation.class, UpdateValidation.class})
        @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters", groups = {CreateValidation.class, UpdateValidation.class})
        String address,

        @NotNull(message = "Status cannot be null", groups = {CreateValidation.class, UpdateValidation.class})
        Status status
) {}
