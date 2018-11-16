package ch.adesso.maturity.board.metadata;

import org.junit.rules.ExternalResource;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MaturityClient extends ExternalResource {

    private Client client;
    private WebTarget baseTarget;

    @Override
    protected void before() {
        client = ClientBuilder.newClient();
        baseTarget = client.target("http://localhost:9080/sink/resources/teams");
    }

    public JsonObject retrieveMaxLeadTime(String teamId) {
        Response response = baseTarget
                .path(teamId)
                .path("maxLeadTime")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        return response.readEntity(JsonObject.class);
    }
}
