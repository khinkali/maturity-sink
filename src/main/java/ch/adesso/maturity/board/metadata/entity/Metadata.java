package ch.adesso.maturity.board.metadata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.json.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@NamedQueries(
        @NamedQuery(name = Metadata.NAMED_QUERIES.FIND_ALL, query = "SELECT m FROM Metadata m")
)
@NoArgsConstructor
@Getter
@ToString
@Entity
public class Metadata {
    public static final class NAMED_QUERIES {
        public static final String FIND_ALL = "Metadata.findAll";
    }

    public static final class JSON_KEYS {
        public static final String ID = "id";
        public static final String LABELS = "labels";
        public static final String PAYLOAD = "payload";
    }

    @Id
    @Setter
    private String id;
    @OneToMany(cascade = CascadeType.ALL)
    private Map<String, StringValue> labels;
    @OneToMany(cascade = CascadeType.ALL)
    private Map<String, PropertyValue> payload;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add(JSON_KEYS.ID, id)
                .add(JSON_KEYS.LABELS, labelsAsJson())
                .add(JSON_KEYS.PAYLOAD, payloadAsJson())
                .build();
    }

    public Metadata(JsonObject asJson) {
        this.id = UUID.randomUUID().toString();
        this.labels = asJson.getJsonObject(JSON_KEYS.LABELS)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new StringValue(toJavaType(e.getValue()).toString())));
        this.payload = asJson.getJsonObject(JSON_KEYS.PAYLOAD)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> new PropertyValue(toJavaType(e.getValue()))));
        System.out.println("this = " + this);
    }

    public Serializable toJavaType(JsonValue value) {
        if (value.getValueType() == JsonValue.ValueType.STRING) {
            return ((JsonString) value).getString();
        } else if (value.getValueType() == JsonValue.ValueType.NUMBER) {
            return ((JsonNumber) value).doubleValue();
        } else if (value.getValueType() == JsonValue.ValueType.FALSE) {
            return false;
        } else if (value.getValueType() == JsonValue.ValueType.TRUE) {
            return true;
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