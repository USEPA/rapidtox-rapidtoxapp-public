package gov.epa.ccte.api.rapidtox.physchem.repository;

import gov.epa.ccte.api.rapidtox.physchem.model.SimilarityPhychemAggrER;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SimilarityPhychemAggrRepositoryER extends ReadOnlyRepository<SimilarityPhychemAggrER, String> {

    // @RestResource(path = "by-dtxsids", rel = "find-by-dtxsids")
    List<SimilarityPhychemAggrER> findAllByDtxsidIn(List<String> dtxsid);
}
