package ch.adesso.maturity.board.devops_maturity.entity;

import ch.adesso.maturity.board.Maturity;
import ch.adesso.maturity.board.maturity.entity.Service;

import javax.json.Json;
import javax.json.JsonObject;

public class Efficiency implements Maturity {
    private final String id;
    private final String name;
    private final Double minEfficiency;
    private final Service minEfficiencyService;

    public static final class JSON_KEYS {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String MIN_EFFICIENCY = "minEfficiency";
        public static final String MIN_EFFICIENCY_SERVICE = "service";
    }

    public Efficiency(Service minEfficencyService) {
        this.id = "minEfficiency";
        this.name = "Minimum Efficiency";
        this.minEfficiencyService = minEfficencyService;
        this.minEfficiency = 0.33;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.ID, this.id)
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.MIN_EFFICIENCY, this.minEfficiency)
                .add(JSON_KEYS.MIN_EFFICIENCY_SERVICE, this.minEfficiencyService.toJson())
                .build();
    }
}
