package com.cts.schlmgmt.models;

import com.cts.schlmgmt.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "UserID")
    private UUID userId;

    @Column(name = "Name")
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "Role")
    private Role role;

    @Email
    @NotNull
    @Column(unique = true, name = "Email")
    private String email;

    @NotNull
    @Column(unique = true, name = "Phone")
    private long phone;

    @NotNull
    @Column(name = "Status")
    private boolean status;

    @NotNull
    @Column(name = "Created At")
    private LocalDate createdAt;

    @NotNull
    @Column(name = "Updated At")
    private LocalDate updatedAt;


}
