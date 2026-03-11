package com.cts.edusphere.modules;

import com.cts.edusphere.core.BaseEntity;
import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "user_id"))

public class User extends BaseEntity {
    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false, name = "name")
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Set<Role> roles;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be 7 - 15 digits")
    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @NotBlank(message = "Password cannot be blank")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, name = "password")
    private String password;
}
