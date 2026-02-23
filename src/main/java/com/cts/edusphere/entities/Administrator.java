package com.cts.edusphere.entities;

import com.cts.edusphere.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "admin")
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "adminId")
@EntityListeners(AuditingEntityListener.class)

public class Administrator extends User {

    @Column(nullable = false)
    private int adminId;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
