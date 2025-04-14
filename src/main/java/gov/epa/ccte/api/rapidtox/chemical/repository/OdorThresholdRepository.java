package gov.epa.ccte.api.rapidtox.chemical.repository;

import gov.epa.ccte.api.rapidtox.chemical.model.OdorThreshold;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface OdorThresholdRepository extends ReadOnlyRepository<OdorThreshold, String> {

    Optional<OdorThreshold> findByDtxsidAndOdorThresholdIsNotNull(String dtxsid);
}
