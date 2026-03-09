package com.cts.edusphere.common.dto.Faculty;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record FacultyRequestDTO(
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

        @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Position cannot be blank")
        String position,

        @NotNull(groups = OnCreate.class, message = "Department ID cannot be null")
        UUID departmentId,

        @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Status cannot be null")
        Status status
) {}