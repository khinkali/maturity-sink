package ch.adesso.maturity.board.team.boundary;

import ch.adesso.maturity.board.maturity.boundary.MaturityCalculator;
import ch.adesso.maturity.board.maturity.entity.Service;
import ch.adesso.maturity.board.team.entity.Team;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.stream.JsonCollectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Stateless
@Path("teams")
public class TeamResource {

    @PersistenceContext
    EntityManager em;

    @Context
    UriInfo uriinfo;

    @Inject
    MaturityCalculator calculator;

    @GET
    public JsonArray getTeams() {
        return em.createNamedQuery(Team.NAMED_QUERIES.FIND_ALL, Team.class)
                .getResultList()
                .stream()
                .map(t -> t.toJson())
                .collect(JsonCollectors.toJsonArray());
    }

    @POST
    public Response create(JsonObject teamAsJson) {
        Team team = new Team(teamAsJson);
        em.merge(team);
        URI uri = uriinfo.getBaseUriBuilder()
                .path(TeamResource.class)
                .path("{id}")
                .build(team.getId());
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public JsonObject get(@PathParam("id") String id) {
        return em.find(Team.class, id).toJson();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") String id) {
        em.remove(em.find(Team.class, id));
        return Response.noContent().build();
    }

    @Path("{id}/maxLeadTime")
    @GET
    public JsonObject getMaxLeadTime(@PathParam("id") String teamId) {
        Service maxLeadTime = calculator.getMaxLeadTime(teamId);
        return Json.createObjectBuilder()
                .add("maxLeadTime", maxLeadTime.getMaxLeadTimeInMs())
                .add("service", maxLeadTime.getName())
                .build();
    }
}
