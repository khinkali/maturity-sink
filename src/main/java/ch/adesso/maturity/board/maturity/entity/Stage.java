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
public class Stage {
    private final List<ExecutionStep> executionSteps;
    private final String name;

    public static final class JSON_KEYS {
        public static final String NAME = "name";
        public static final String START = "start";
        public static final String END = "end";
        public static final String CYCLE_TIME_IN_MS = "cycleTimeInMs";
        public static final String LEAD_TIME_IN_MS = "leadTimeInMs";
        public static final String EXECUTION_STEPS = "executionSteps";
    }

    public Stage(String name, List<Metadata> metadata) {
        Map<String, List<Metadata>> metadataByExecutionSteps = new HashMap<>();
        for (Metadata data : metadata) {
            String executionStep = data.getExecutionStep();
            if (!metadataByExecutionSteps.containsKey(executionStep)) {
                metadataByExecutionSteps.put(executionStep, new ArrayList<>());
            }
            metadataByExecutionSteps.get(executionStep).add(data);
        }
        this.executionSteps = new ArrayList<>();
        for (String executionStep : metadataByExecutionSteps.keySet()) {
            executionSteps.add(new ExecutionStep(metadataByExecutionSteps.get(executionStep)));
        }
        this.name = name;
    }

    public ExecutionStep getStart() {
        return executionSteps.stream()
                .min(Comparator.comparing(ExecutionStep::getStart))
                .get();
    }

    public LocalDateTime getStartTime() {
        return getStart().getStart();
    }

    public ExecutionStep getEnd() {
        return executionSteps.stream()
                .max(Comparator.comparing(ExecutionStep::getEnd))
                .get();
    }

    public LocalDateTime getEndTime() {
        return getEnd().getEnd();
    }

    public Long getCycleTimeInMs() {
        return executionSteps.stream()
                .map(ExecutionStep::getTimeInMs)
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
                .add(JSON_KEYS.EXECUTION_STEPS, this.executionSteps.stream()
                        .map(ExecutionStep::toJson)
                        .collect(JsonCollectors.toJsonArray()))
                .build();
    }
}
