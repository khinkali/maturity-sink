package ch.adesso.maturity.board.metadata.entity;

import ch.adesso.maturity.board.team.entity.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.json.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@NamedQueries({
        @NamedQuery(name = Metadata.NAMED_QUERIES.FIND_ALL, query = "SELECT m FROM Metadata m"),
        @NamedQuery(name = Metadata.NAMED_QUERIES.FIND_BY_TEAM, query = "SELECT m FROM Metadata m WHERE m.team.id = :teamId")
})
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Metadata {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    public static final class NAMED_QUERIES {
        public static final String FIND_ALL = "Metadata.findAll";
        public static final String FIND_BY_TEAM = "Metadata.findByTeam";
    }

    public static final class JSON_KEYS {
        public static final String ID = "id";
        public static final String CREATION = "creation";
        public static final String LABELS = "labels";
        public static final String PAYLOAD = "payload";
        public static final String TEAM = "team";
    }

    public static final class LABEL_KEYS {
        public static final String SERVICE = "service";
        public static final String VERSION = "version";
        public static final String STAGE = "stage";
        public static final String EXECUTION_STEP = "execution-step";
    }

    public static final class PAYLOAD_KEYS {
        public static final String TIME_IN_MS = "time-in-ms";
    }

    @Id
    @Setter
    private String id;
    private LocalDateTime creation;
    private Team team;
    @OneToMany(cascade = CascadeType.ALL)
    private Map<String, StringValue> labels;
    @OneToMany(cascade = CascadeType.ALL)
    private Map<String, PropertyValue> payload;

    public Long getTimeInMs() {
        return ((Double) this.payload
                .get(PAYLOAD_KEYS.TIME_IN_MS)
                .getValue()).longValue();
    }

    public String getExecutionStep() {
        return this.labels
                .get(LABEL_KEYS.EXECUTION_STEP)
                .getValue();
    }

    public String getStage() {
        return this.labels
                .get(LABEL_KEYS.STAGE)
                .getValue();
    }

    public String getVersion() {
        return this.labels
                .get(LABEL_KEYS.VERSION)
                .getValue();
    }

    public String getService() {
        return this.labels
                .get(LABEL_KEYS.SERVICE)
                .getValue();
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.ID, id)
                .add(JSON_KEYS.LABELS, labelsAsJson())
                .add(JSON_KEYS.PAYLOAD, payloadAsJson())
                .add(JSON_KEYS.CREATION, creation.format(FORMATTER))
                .add(JSON_KEYS.TEAM, team.getId())
                .build();
    }

    public Metadata(JsonObject asJson, Team team) {
        this.id = UUID.randomUUID().toString();
        String creation = asJson.getString("creation", null);
        if (creation != null) {
            this.creation = LocalDateTime.parse(creation, FORMATTER);
        } else {
            this.creation = LocalDateTime.now();
        }
        this.labels = asJson.getJsonObject(JSON_KEYS.LABELS)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new StringValue(toJavaType(e.getValue()).toString())));
        this.payload = asJson.getJsonObject(JSON_KEYS.PAYLOAD)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new PropertyValue(toJavaType(e.getValue()))));
        this.team = team;
    }

    public Serializable toJavaType(JsonValue value) {
        if (value.getValueType() == JsonValue.ValueType.STRING) {
            return ((JsonString) value).getString();
        } else if (value.getValueType() == JsonValue.ValueType.NUMBER) {
            return ((JsonNumber) value).doubleValue();
        } else if (value.getValueType() == JsonValue.ValueType.NULL) {
            return null;
        } else if (value.getValueType() == JsonValue.ValueType.FALSE) {
            return false;
        } else if (value.getValueType() == JsonValue.ValueType.TRUE) {
            return true;
        } else if (value.getValueType() == JsonValue.ValueType.ARRAY) {
            JsonArray jsonArray = value.asJsonArray();
            return (Serializable) Arrays.asList(jsonArray.toArray())
                    .stream()
                    .map(obj -> (Serializable) toJavaType((JsonValue) obj))
                    .filter(obj -> obj instanceof Serializable)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException(value.getValueType() + " is not implemented yet!");
        }
    }

    public JsonObject labelsAsJson() {
        Map<String, Object> jsonLabels = this.labels.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getValue()));
        return Json.createObjectBuilder(jsonLabels).build();
    }

    public JsonObject payloadAsJson() {
        Map<String, Object> jsonPayload = this.payload.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getValue()));
        return Json.createObjectBuilder(jsonPayload).build();
    }
}
