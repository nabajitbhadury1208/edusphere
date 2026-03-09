package com.cts.edusphere.common.dto.Student;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Gender;
import com.cts.edusphere.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record StudentRequestDTO(
        @NotBlank(groups = OnCreate.class, message = "Name cannot be blank")
        String name,

        @Email(groups = OnCreate.class, message = "Email should be valid")
        @NotBlank(groups = OnCreate.class, message = "Email cannot be blank")
        String email,

        @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^\\+?\\d{7,15}$", message = "Phone number must be 7 - 15 digits")
        String phone,

        @NotBlank(groups = OnCreate.class, message = "Password cannot be blank")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password,

        @NotNull(groups = OnCreate.class, message = "Date of birth cannot be null")
        @PastOrPresent(groups = {OnCreate.class, OnUpdate.class}, message = "Date of birth must be in the past")
        LocalDate dob,

        @NotNull(groups = {OnCreate.class}, message = "Gender cannot be null")
        Gender gender,

        @NotBlank(groups = {OnCreate.class}, message = "Address cannot be blank")
        String address,

        @NotNull(groups = {OnCreate.class}, message = "Status cannot be null")
        Status status
) {
}
