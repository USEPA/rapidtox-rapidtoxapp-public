package gov.epa.ccte.api.rapidtox.physchem.repository;

import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemSummary;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhyschemSummaryRepository extends ReadOnlyRepository<PhyschemSummary, String> {

    // @RestResource(path = "by-dtxsid", rel = "find-by-dtxsid")
    List<PhyschemSummary> findByDtxsidOrderByPreferredNameAscPropertyAsc(@Param("dtxsid") String dtxsid);

    // @RestResource(path = "by-dtxsids", rel = "find-by-dtxsids")
    List<PhyschemSummary> findByDtxsidInOrderByPreferredNameAscPropertyAsc(List<String> dtxsid);

    // @RestResource(path = "by-dtxsid-with-property", rel = "find-by-dtxsid-with-property")
    List<PhyschemSummary> findByDtxsidAndProperty(@Param("dtxsid") String dtxsid, @Param("property") String property);

    // @RestResource(path = "by-dtxsid-and-properties", rel = "find-by-dtxsid-and-properties")
    List<PhyschemSummary> findByDtxsidAndPropertyInOrderByPropertyAsc(String dtxsid, List<String> propertyNames);
}
