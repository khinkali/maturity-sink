package ch.adesso.maturity.board.maturity.entity;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import lombok.Getter;

import java.util.*;

@Getter
public class Service {
    private final List<Version> versions;
    private final String name;

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
                .map(Version::getCycleTime)
                .max(Comparator.comparing(Long::valueOf))
                .get();
    }

    public Long getMaxLeadTimeInMs() {
        return versions.stream()
                .map(Version::getLeadTimeInMs)
                .max(Comparator.comparing(Long::valueOf))
                .get();
    }
}

