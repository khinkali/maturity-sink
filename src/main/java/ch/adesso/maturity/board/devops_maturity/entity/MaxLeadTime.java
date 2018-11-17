package ch.adesso.maturity.board.devops_maturity.entity;

import ch.adesso.maturity.board.Maturity;
import ch.adesso.maturity.board.maturity.entity.Service;

import javax.json.Json;
import javax.json.JsonObject;

public class MaxLeadTime implements Maturity {
    private final String name;
    private final Long maxLeadTimeInMs;
    private final Service maxLeadTime;

    public static final class JSON_KEYS {
        public static final String NAME = "name";
        public static final String MAX_LEAD_TIME_IN_MS = "maxLeadTimeInMs";
        public static final String MAX_LEAD_TIME = "maxLeadTime";
    }

    public MaxLeadTime(Service maxLeadTime) {
        this.name = "Maximum Lead Time";
        this.maxLeadTimeInMs = 2 * 24 * 60 * 60 * 1_000L;
        this.maxLeadTime = maxLeadTime;
    }

    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.MAX_LEAD_TIME_IN_MS, this.maxLeadTimeInMs)
                .add(JSON_KEYS.MAX_LEAD_TIME, this.maxLeadTime.toJson())
                .build();
    }
}
