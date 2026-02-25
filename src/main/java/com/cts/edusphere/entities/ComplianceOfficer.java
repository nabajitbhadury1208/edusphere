package com.cts.edusphere.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name="compliance_officers")
@PrimaryKeyJoinColumn(name = "user_id")

public class ComplianceOfficer extends User{


}
