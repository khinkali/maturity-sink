package ch.adesso.maturity.board.metadata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Entity
public class StringValue {
    @Id
    private String id;
    @Getter
    private String value;

    public StringValue(String value) {
        this.id = UUID.randomUUID().toString();
        this.value = value;
    }
}
