package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Getter
public class Version {
    private final List<Stage> stages;
    private final String name;

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

    public Long getCycleTime() {
        return stages.stream()
                .map(Stage::getCycleTimeInMs)
                .reduce(0L, Long::sum);
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

    public Long getLeadTimeInMs() {
        return getStartTime().until(getEndTime(), ChronoUnit.MILLIS);
    }
}
