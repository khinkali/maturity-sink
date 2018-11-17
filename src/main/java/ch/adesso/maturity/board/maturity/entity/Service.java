package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import java.util.*;

@Getter
public class Service {
    private final List<Version> versions;
    private final String name;

    public static final class JSON_KEYS {
        public static final String NAME = "name";
        public static final String MAX_CYCLE_TIME_IN_MS = "maxCycleTimeInMs";
        public static final String MAX_LEAD_TIME_IN_MS = "maxLeadTimeInMs";
        public static final String VERSIONS = "versions";
    }

    public Service(String name, List<Metadata> metadata) {
        Map<String, List<Metadata>> metadataByVersion = new HashMap<>();
        for (Metadata data : metadata) {
            String version = data.getVersion();
            if (!metadataByVersion.containsKey(version)) {
                metadataByVersion.put(version, new ArrayList<>());
            }
            metadataByVersion.get(version).add(data);
        }
        this.versions = new ArrayList<>();
        for (String version : metadataByVersion.keySet()) {
            versions.add(new Version(version, metadataByVersion.get(version)));
        }
        this.name = name;
    }

    public Long getMaxCycleTimeInMs() {
        return versions.stream()
                .map(Version::getCycleTimeInMs)
                .max(Comparator.comparing(Long::valueOf))
                .get();
    }

    public Long getMaxLeadTimeInMs() {
        return versions.stream()
                .map(Version::getLeadTimeInMs)
                .max(Comparator.comparing(Long::valueOf))
                .get();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.NAME, this.name)
                .add(JSON_KEYS.MAX_CYCLE_TIME_IN_MS, getMaxCycleTimeInMs())
                .add(JSON_KEYS.MAX_LEAD_TIME_IN_MS, getMaxLeadTimeInMs())
                .add(JSON_KEYS.VERSIONS, this.versions.stream()
                        .map(Version::toJson)
                        .collect(JsonCollectors.toJsonArray()))
                .build();
    }
}

