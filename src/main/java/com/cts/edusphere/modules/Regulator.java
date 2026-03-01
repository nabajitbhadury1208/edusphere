package com.cts.edusphere.modules;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@SuperBuilder
@Table(name="regulators")
@PrimaryKeyJoinColumn(name = "user_id")
@NoArgsConstructor

public class Regulator extends User {

}
