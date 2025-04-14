package gov.epa.ccte.api.rapidtox.bioactivity.repository;

import gov.epa.ccte.api.rapidtox.bioactivity.model.InvitroDbSummary;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitroDbSummaryRepository extends ReadOnlyRepository<InvitroDbSummary, Long> {

    public List<InvitroDbSummary> findAllByDtxsidIn(List<String> dtxsids);

    public Optional<InvitroDbSummary> findByDtxsid(String dtxsid);
}
