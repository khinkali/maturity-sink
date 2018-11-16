package ch.adesso.maturity.board.maturity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
public class MaxLeadTimeMaturity {
    @Id
    private String id;
    private String name;
    private Long maxLeadTimeInSeconds;
}
