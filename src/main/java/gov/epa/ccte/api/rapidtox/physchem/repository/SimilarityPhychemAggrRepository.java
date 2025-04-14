package gov.epa.ccte.api.rapidtox.physchem.repository;

import gov.epa.ccte.api.rapidtox.physchem.model.SimilarityPhychemAggr;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SimilarityPhychemAggrRepository extends ReadOnlyRepository<SimilarityPhychemAggr, String> {

    // @RestResource(path = "by-dtxsids", rel = "find-by-dtxsids")
    List<SimilarityPhychemAggr> findAllByDtxsidIn(List<String> dtxsid);

}
