package ch.adesso.maturity.board.devops_maturity.entity;

import ch.adesso.maturity.board.Maturity;
import ch.adesso.maturity.board.maturity.entity.Service;

import javax.json.Json;
import javax.json.JsonObject;

public class MaxCycleTime implements Maturity {
    private final String id;
    private final String name;
    private final Long maxCycleTimeInMs;
    private final Service maxCycleTime;

    public static final class JSON_KEYS {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String MAX_CYCLE_TIME_IN_MS = "maxCycleTimeInMs";
        public static final String MAX_CYCLE_TIME = "service";
    }

    public MaxCycleTime(Service maxCycleTime) {
        this.id = "maxCycleTime";
        this.name = "Maximum Cycle Time";
        this.maxCycleTime = maxCycleTime;
        this.maxCycleTimeInMs = 60 * 60 * 1_000L;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.ID, this.id)
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.MAX_CYCLE_TIME_IN_MS, this.maxCycleTimeInMs)
                .add(JSON_KEYS.MAX_CYCLE_TIME, this.maxCycleTime.toJson())
                .build();
    }
}
