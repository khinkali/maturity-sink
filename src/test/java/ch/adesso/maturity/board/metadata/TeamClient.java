package ch.adesso.maturity.board.metadata;

import org.junit.rules.ExternalResource;
import sun.security.krb5.internal.MethodData;

import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TeamClient extends ExternalResource {

    private WebTarget baseTarget;
    private Client client;

    @Override
    protected void before() {
        client = ClientBuilder.newClient();
        baseTarget = client.target("http://localhost:9080/sink/resources/teams");
    }

    public URI create(JsonObject teamAsJson) {
        Response resonse = baseTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(teamAsJson));
        assertThat(resonse.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        return resonse.getLocation();
    }

    public JsonObject retrieve(URI uri) {
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        return response.readEntity(JsonObject.class);
    }
}
