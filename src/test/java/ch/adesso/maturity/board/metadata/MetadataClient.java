package ch.adesso.maturity.board.metadata;

import org.junit.rules.ExternalResource;

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

public class MetadataClient extends ExternalResource {

    private Client client;
    private WebTarget baseTarget;

    @Override
    protected void before() {
        this.client = ClientBuilder.newClient();
        baseTarget = client.target("http://localhost:9080/sink/resources/metadata");
    }

    public URI create(JsonObject data) {
        Response response = baseTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(data));
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        return response.getLocation();
    }

    public JsonObject retrieveMetadata(URI uri) {
        Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        return response.readEntity(JsonObject.class);
    }
}
