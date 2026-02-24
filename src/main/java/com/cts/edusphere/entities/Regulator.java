package com.cts.edusphere.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@SuperBuilder
@Table(name="regulators")
@PrimaryKeyJoinColumn(name = "user_id")
@NoArgsConstructor

public class Regulator extends User {

}
