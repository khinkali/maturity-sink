package ch.adesso.maturity.board.team.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.json.Json;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.UUID;

@NamedQueries(
        @NamedQuery(name = Team.NAMED_QUERIES.FIND_ALL, query = "SELECT t FROM Team t")
)
@Entity
@NoArgsConstructor
@Getter
public class Team {
    @Id
    private String id;
    private String name;

    public static final class JSON_KEYS {
        public static final String ID = "id";
        public static final String NAME = "name";
    }

    public static final class NAMED_QUERIES {
        public static final String FIND_ALL = "Team.findAll";
    }

    public Team(JsonObject json) {
        this.id = UUID.randomUUID().toString();
        this.name = json.getString(JSON_KEYS.NAME);
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.ID, this.id)
                .add(JSON_KEYS.NAME, this.name)
                .build();
    }
}
