package ch.adesso.maturity.board.metadata;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MetadataIT {

    @Rule
    public MetadataClient metadataClient = new MetadataClient();
    @Rule
    public TeamClient teamClient = new TeamClient();
    @Rule
    public MaturityClient maturityClient = new MaturityClient();

    private static URI firstMetadata;
    private static JsonObject teamA;

    @Test
    public void a10_shouldCreateTeam() {
        JsonObject team = Json.createObjectBuilder()
                .add("name", "Team A")
                .build();
        URI uri = teamClient.create(team);
        teamA = teamClient.retrieve(uri);
    }

    @Test
    public void a20_shouldInsertValidMetadata() {
        JsonObject labels = Json.createObjectBuilder()
                .add("stage", "checkout & unit tests & builds")
                .add("service", "sink")
                .add("execution-step", "git-clone")
                .add("version", "0.5.29")
                .build();
        JsonObject payload = Json.createObjectBuilder()
                .add("commits-of-current-tag", "bb0fe77")
                .add("time-in-ms", 18045)
                .build();
        JsonObject metadata = Json.createObjectBuilder()
                .add("labels", labels)
                .add("payload", payload)
                .add("team", teamA.getString("id"))
                .build();
        firstMetadata = metadataClient.create(metadata);
    }

    @Test
    public void a30_shouldRetrieveCreatedMetadata() {
        JsonObject data = metadataClient.retrieveMetadata(firstMetadata);
        JsonObject labels = data.getJsonObject("labels");
        JsonObject payload = data.getJsonObject("payload");

        assertThat(labels, is(notNullValue()));
        assertThat(payload, is(notNullValue()));

        assertThat(labels.getString("stage"), is("checkout & unit tests & builds"));
        assertThat(labels.getString("service"), is("sink"));
        assertThat(labels.getString("execution-step"), is("git-clone"));
        assertThat(labels.getString("version"), is("0.5.29"));

        assertThat(payload.getString("commits-of-current-tag"), is("bb0fe77"));
        assertThat(payload.getJsonNumber("time-in-ms").longValue(), is(18045L));

        assertThat(data.getString("creation"), is(notNullValue()));
        assertThat(data.getString("id"), is(notNullValue()));
        assertThat(data.getString("team"), is(teamA.getString("id")));
    }

    @Test
    public void a40_shouldLoadTestData() throws IOException {
        parseJsonFile("src/test/resources/testdata.json")
                .getValuesAs(JsonObject.class)
                .stream()
                .forEach(obj -> metadataClient.create(obj));
    }

    @Test
    public void a50_shouldCalculateTheMaxLeadTime() {
        JsonArray maxLeadTime = maturityClient.retrieveMaxLeadTime(teamA.getString("id"));
        System.out.println("maxLeadTime = " + maxLeadTime);
    }

    public JsonArray parseJsonFile(String filename) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filename));
        String textContent = new String(fileBytes, StandardCharsets.UTF_8);
        textContent = textContent.replaceAll("~TEAM~", teamA.getString("id"));
        ByteArrayInputStream content = new ByteArrayInputStream(textContent.getBytes());
        return Json
                .createReader(content)
                .readArray();
    }
}
