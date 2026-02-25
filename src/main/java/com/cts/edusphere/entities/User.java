package com.cts.edusphere.entities;

import com.cts.edusphere.enums.Role;
import com.cts.edusphere.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "user_id"))

public class User extends BaseEntity{
    @NotBlank(message = "Name cannot be blank")
    @Column(nullable = false, name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

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
   @Column(nullable = false, name = "passowrd")
   private String password;
}
