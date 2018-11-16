package ch.adesso.maturity.board.maturity.boundary;

import ch.adesso.maturity.board.metadata.entity.Metadata;
import ch.adesso.maturity.board.team.entity.Team;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class MaturityResource {

    @PersistenceContext
    EntityManager em;

    public Long calculateMaxLeadTime(Team team) {
        List<Metadata> metadataByTeam = em.createNamedQuery(Metadata.NAMED_QUERIES.FIND_BY_TEAM, Metadata.class)
                .getResultList();

        Map<String, List<Metadata>> metadataByService = new HashMap<>();
        for (Metadata data : metadataByTeam) {
            String service = data.getService();
            if (!metadataByService.containsKey(service)) {
                metadataByService.put(service, new ArrayList<>());
            }
            metadataByService.get(service).add(data);
        }
        return 0L;
    }
}
