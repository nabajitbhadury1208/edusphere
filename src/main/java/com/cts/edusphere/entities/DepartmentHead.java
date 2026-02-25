package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name="department_heads")
@PrimaryKeyJoinColumn(name = "user_id")

public class DepartmentHead extends User{

}
