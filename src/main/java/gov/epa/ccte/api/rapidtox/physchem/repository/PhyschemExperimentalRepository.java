package gov.epa.ccte.api.rapidtox.physchem.repository;

import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemExperimental;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhyschemExperimentalRepository extends ReadOnlyRepository<PhyschemExperimental, Integer> {

    // @RestResource(path = "by-dtxsid-with-property", rel = "find-by-dtxsid-with-property", exported = true)
    List<PhyschemExperimental> findByDtxsidAndProperty(@Param("dtxsid") String dtxsid, @Param("property") String property);

    // @RestResource(path = "by-dtxsids-with-property", rel = "find-by-dtxsids-with-property")
    List<PhyschemExperimental> findByDtxsidInAndPropertyEquals(List<String> dtxsids, String property);

    // @RestResource(path = "by-dtxsids", rel = "find-by-dtxsids")
    List<PhyschemExperimental> findByDtxsidInOrderByDtxsidAscPropertyAsc(List<String> dtxsids);
}
