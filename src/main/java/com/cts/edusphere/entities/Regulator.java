package com.cts.edusphere.entities;
import com.cts.edusphere.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Getter
@Setter
@SuperBuilder
@Table(name="regulator")
@PrimaryKeyJoinColumn(name = "regulatorId")
@EntityListeners(AuditingEntityListener.class)

public class Regulator extends User {

    @Column(nullable = false)
    private int regulatorId;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
