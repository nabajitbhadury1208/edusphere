package com.cts.edusphere.modules;

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
