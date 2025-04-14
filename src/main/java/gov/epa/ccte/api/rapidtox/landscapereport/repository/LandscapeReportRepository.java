package gov.epa.ccte.api.rapidtox.landscapereport.repository;

import gov.epa.ccte.api.rapidtox.landscapereport.model.LandscapeReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LandscapeReportRepository extends JpaRepository<LandscapeReport, Integer> {

    List<LandscapeReport> findBySessionKey(String sessionKey);

    List<LandscapeReport> findByDtxsidAndWorkflow(String dtxsid, String workflow);

    boolean existsByDtxsidAndWorkflow(String dtxsid, String workflow);

    boolean existsByDtxsidAndWorkflowAndGenerate(String dtxsid, String workflow, boolean generate);

    LandscapeReport findFirstByDtxsidAndWorkflowOrderByVersionDesc(@Param("dtxsid") String dtxsid, @Param("workflow") String workflow);
}
