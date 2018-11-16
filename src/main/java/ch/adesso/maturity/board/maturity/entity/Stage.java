package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Getter
public class Stage {
    private final List<ExecutionStep> executionSteps;
    private final String name;

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

    public Long getCycleTimeInMs() {
        return executionSteps.stream()
                .map(ExecutionStep::getTimeInMs)
                .reduce(0L, Long::sum);
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

    public Long getLeadTimeInMs() {
        return getStartTime().until(getEndTime(), ChronoUnit.MILLIS);
    }
}
