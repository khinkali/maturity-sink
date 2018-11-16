package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ExecutionStep {
    private final Long timeInMs;
    private final LocalDateTime start;
    private final String name;

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
}
