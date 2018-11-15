package ch.adesso.maturity.board.metadata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@ToString
@NoArgsConstructor
@Entity
public class PropertyValue {
    @Id
    private String id;
    @Getter
    private Serializable value;

    public PropertyValue(Serializable value) {
        this.id = UUID.randomUUID().toString();
        this.value = value;
    }
}
