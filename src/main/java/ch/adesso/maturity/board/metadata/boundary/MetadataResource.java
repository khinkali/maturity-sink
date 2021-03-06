package ch.adesso.maturity.board.metadata.boundary;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import ch.adesso.maturity.board.team.entity.Team;

import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Path("metadata")
public class MetadataResource {

    @PersistenceContext
    EntityManager em;

    @Context
    UriInfo uriinfo;

    @GET
    public JsonArray retrieveMetadata() {
        return em.createNamedQuery(Metadata.NAMED_QUERIES.FIND_ALL, Metadata.class)
                .getResultList()
                .stream()
                .map(Metadata::toJson)
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response createMetadata(JsonObject dataAsJson) {
        String teamId = dataAsJson.getString(Metadata.JSON_KEYS.TEAM);
        Team team = em.find(Team.class, teamId);
        Metadata data = new Metadata(dataAsJson, team);
        em.merge(data);
        URI uri = uriinfo.getBaseUriBuilder()
                .path(MetadataResource.class)
                .path("{id}")
                .build(data.getId());
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public JsonObject retrieveMetadata(@PathParam("id") String id) {
        return em.find(Metadata.class, id)
                .toJson();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        em.remove(em.find(Metadata.class, id));
        return Response.noContent().build();
    }

}
