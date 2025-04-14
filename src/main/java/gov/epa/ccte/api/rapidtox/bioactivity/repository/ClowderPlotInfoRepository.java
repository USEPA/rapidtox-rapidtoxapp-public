package gov.epa.ccte.api.rapidtox.bioactivity.repository;

import gov.epa.ccte.api.rapidtox.bioactivity.model.ClowderPlotInfo;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ClowderPlotInfoRepository extends ReadOnlyRepository<ClowderPlotInfo, Integer> {

    List<ClowderPlotInfo> findByDtxsid(String dtxsid);
}
