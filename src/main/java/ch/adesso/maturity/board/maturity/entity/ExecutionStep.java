package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import javax.json.Json;
import javax.json.JsonObject;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ExecutionStep {
    private final Long timeInMs;
    private final LocalDateTime start;
    private final String name;

    public static final class JSON_KEYS {
        public static final String NAME = "name";
        public static final String TIME_IN_MS = "timeInMs";
        public static final String START = "start";
        public static final String END = "end";
    }

    public ExecutionStep(List<Metadata> metadata) {
        if (metadata.size() != 1) {
            throw new IllegalArgumentException("There can only be one Metadata for each ExecutionStep");
        }
        Metadata current = metadata.get(0);
        this.timeInMs = current.getTimeInMs();
        this.start = current.getCreation();
        this.name = current.getExecutionStep();
    }

    public LocalDateTime getEnd() {
        return getStart().plusNanos(1_000_000L * getTimeInMs());
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.START, this.start.format(Metadata.FORMATTER))
                .add(JSON_KEYS.END, getEnd().format(Metadata.FORMATTER))
                .add(JSON_KEYS.TIME_IN_MS, getTimeInMs())
                .build();
    }
}
