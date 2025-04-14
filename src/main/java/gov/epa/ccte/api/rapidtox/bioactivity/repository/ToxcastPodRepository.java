package gov.epa.ccte.api.rapidtox.bioactivity.repository;
import gov.epa.ccte.api.rapidtox.bioactivity.model.Toxcast;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ToxcastPodRepository extends ReadOnlyRepository<Toxcast, Integer> {
    // @RestResource(path = "by-dtxsid", rel = "find-by-dtxsid")
    List<Toxcast> findByDtxsid(String dtxsid);

    // @RestResource(path = "by-dtxsids", rel = "find-by-dtxsids")
    List<Toxcast> findByDtxsidIn(List<String> dtxsids);
}
