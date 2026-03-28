package com.cts.edusphere.common.dto.student;

import com.cts.edusphere.common.validation.OnCreate;
import com.cts.edusphere.common.validation.OnUpdate;
import com.cts.edusphere.enums.Gender;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating or updating a student's account.
 *
 * @param name     the full name of the student; required on create
 * @param email    the unique email address; must be a valid email format; required on create
 * @param phone    the contact phone number; must be 7 to 15 digits with an optional leading '+'; validated on create and update
 * @param password the account password; write-only and never returned in responses; required on create
 * @param dob      the student's date of birth; must be in the past or today; required on create
 * @param gender   the student's gender identity (MALE, FEMALE, or PREFER_NOT_TO_SAY); required on create
 * @param address  the student's residential or mailing address; required on create
 */
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
        String address


) {
}
