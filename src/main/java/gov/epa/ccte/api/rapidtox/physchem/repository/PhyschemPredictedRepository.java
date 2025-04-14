package gov.epa.ccte.api.rapidtox.physchem.repository;

import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemPredicted;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhyschemPredictedRepository extends ReadOnlyRepository<PhyschemPredicted, Integer> {

    // @RestResource(path = "by-dtxsid-with-property", rel = "find-by-dtxsid-with-property", exported = true)
    List<PhyschemPredicted> findByDtxsidAndProperty(@Param("dtxsid") String dtxsid, @Param("property") String property);

    // @RestResource(path = "by-dtxsids", rel = "find-by-dtxsids")
    List<PhyschemPredicted> findByDtxsidInOrderByDtxsidAscPropertyAsc(List<String> dtxsids);

    List<PhyschemPredicted> findByDtxsidEqualsAndSourceEqualsAndPropertyInIgnoreCase(String dtxsid, String source, List<String> property);
}
