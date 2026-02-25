package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "administrators")
@PrimaryKeyJoinColumn(name = "user_id")

public class Administrator extends User {

}
