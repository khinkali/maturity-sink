package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Getter
public class Version {
    private final List<Stage> stages;
    private final String name;

    public static final class JSON_KEYS {
        public static final String NAME = "name";
        public static final String START = "start";
        public static final String END = "end";
        public static final String CYCLE_TIME_IN_MS = "cycleTimeInMs";
        public static final String LEAD_TIME_IN_MS = "leadTimeInMs";
        public static final String STAGES = "stages";
    }

    public Version(String name, List<Metadata> metadata) {
        Map<String, List<Metadata>> metadataByStages = new HashMap<>();
        for (Metadata data : metadata) {
            String stage = data.getStage();
            if (!metadataByStages.containsKey(stage)) {
                metadataByStages.put(stage, new ArrayList<>());
            }
            metadataByStages.get(stage).add(data);
        }
        this.stages = new ArrayList<>();
        for (String stage : metadataByStages.keySet()) {
            stages.add(new Stage(stage, metadataByStages.get(stage)));
        }
        this.name = name;
    }

    public Stage getStart() {
        return stages.stream()
                .min(Comparator.comparing(Stage::getStartTime))
                .get();
    }

    public LocalDateTime getStartTime() {
        return getStart().getStartTime();
    }

    public Stage getEnd() {
        return stages.stream()
                .max(Comparator.comparing(Stage::getEndTime))
                .get();
    }

    public LocalDateTime getEndTime() {
        return getEnd().getEndTime();
    }

    public Long getCycleTimeInMs() {
        return stages.stream()
                .map(Stage::getCycleTimeInMs)
                .reduce(0L, Long::sum);
    }

    public Long getLeadTimeInMs() {
        return getStartTime().until(getEndTime(), ChronoUnit.MILLIS);
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.START, getStart().toJson())
                .add(JSON_KEYS.END, getEnd().toJson())
                .add(JSON_KEYS.CYCLE_TIME_IN_MS, getCycleTimeInMs())
                .add(JSON_KEYS.LEAD_TIME_IN_MS, getLeadTimeInMs())
                .add(JSON_KEYS.STAGES, this.stages.stream()
                        .map(Stage::toJson)
                        .collect(JsonCollectors.toJsonArray()))
                .build();
    }
}
