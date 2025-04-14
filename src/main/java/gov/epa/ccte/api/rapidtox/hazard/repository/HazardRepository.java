package gov.epa.ccte.api.rapidtox.hazard.repository;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HazardRepository extends ReadOnlyRepository<Hazard, Integer> {

    List<Hazard> findByDtxsidInOrderByPreferredNameAscSuperCategoryAsc(List<String> dtxsid);

    List<Hazard> findAllBySuperCategoryAndDtxsid(String superCategory, String dtxsid);

    List<Hazard> findByIdInOrderByPreferredNameAscSuperCategoryAsc(List<Integer> ids);

    List<Hazard> findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(List<Integer> ids, String superCategory, String toxvalUnits);

    List<Hazard> findByDtxsidAndSuperCategoryEquals(String dtxsid, String superCategory);

    List<Hazard> findAllByDtxsid(String dtxsid);

    List<Hazard> findAllByDtxsidAndWorkflow(@Param("dtxsid") String dtxsid, @Param("workflow") String workflow);

}
