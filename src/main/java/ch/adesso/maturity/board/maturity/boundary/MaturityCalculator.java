package ch.adesso.maturity.board.maturity.boundary;

import ch.adesso.maturity.board.maturity.entity.Service;
import ch.adesso.maturity.board.metadata.entity.Metadata;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Stateless
public class MaturityCalculator {

    @PersistenceContext
    EntityManager em;

    public List<Service> getServices(String teamId) {
        List<Metadata> metadataByTeam = em.createNamedQuery(Metadata.NAMED_QUERIES.FIND_BY_TEAM, Metadata.class)
                .setParameter("teamId", teamId)
                .getResultList();

        System.out.println("metadataByTeam = " + metadataByTeam);

        Map<String, List<Metadata>> metadataByService = new HashMap<>();
        for (Metadata data : metadataByTeam) {
            String service = data.getService();
            if (!metadataByService.containsKey(service)) {
                metadataByService.put(service, new ArrayList<>());
            }
            metadataByService.get(service).add(data);
        }
        List<Service> services = new ArrayList<>();
        for (String service : metadataByService.keySet()) {
            services.add(new Service(service, metadataByService.get(service)));
        }
        return services;
    }

    public Service getMaxLeadTime(String teamId) {
        return getServices(teamId)
                .stream()
                .max(Comparator.comparing(Service::getMaxLeadTimeInMs))
                .get();
    }

    public Service getCycleTime(String teamId) {
        return getServices(teamId)
                .stream()
                .max(Comparator.comparing(Service::getMaxCycleTimeInMs))
                .get();
    }
}
