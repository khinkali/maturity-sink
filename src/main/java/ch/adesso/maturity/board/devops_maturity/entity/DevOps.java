package ch.adesso.maturity.board.devops_maturity.entity;

import ch.adesso.maturity.board.Maturity;
import ch.adesso.maturity.board.maturity.entity.Service;
import ch.adesso.maturity.board.team.entity.Team;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import java.util.ArrayList;
import java.util.List;

public class DevOps {
    private final String id;
    private final String name;
    private final Team team;
    private final List<Maturity> maturities;

    public static final class JSON_KEYS {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TEAM = "team";
        public static final String MATURITIES = "maturities";
    }

    public DevOps(Team team, Service maxLeadTime, Service maxCycleTime) {
        this.id = "devops";
        this.name = "DevOps Maturity";
        this.team = team;
        this.maturities = new ArrayList<>();
        this.maturities.add(new MaxLeadTime(maxLeadTime));
        this.maturities.add(new MaxCycleTime(maxCycleTime));
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.ID, this.id)
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.TEAM, this.team.getId())
                .add(JSON_KEYS.MATURITIES, this.maturities.stream()
                        .map(Maturity::toJson)
                        .collect(JsonCollectors.toJsonArray()))
                .build();
    }
}
