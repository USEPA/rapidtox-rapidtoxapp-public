package gov.epa.ccte.api.rapidtox.sessionreport.repository;

import gov.epa.ccte.api.rapidtox.sessionreport.model.SessionReport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionReportRepository extends JpaRepository<SessionReport, Integer> {

    List<SessionReport> findBySessionKey(String sessionKey);

    List<SessionReport> findAllBySessionKeyInOrderByCreatedAtDesc(List<String> sessionKey);
}
