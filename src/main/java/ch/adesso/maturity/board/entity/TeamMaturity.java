package ch.adesso.maturity.board.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class TeamMaturity {
    @Id
    private String id;
    private String name;

}
