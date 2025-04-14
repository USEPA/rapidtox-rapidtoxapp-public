package gov.epa.ccte.api.rapidtox.session.repository;

import gov.epa.ccte.api.rapidtox.session.model.PrevSession;
import gov.epa.ccte.api.rapidtox.session.model.Session;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    // @RestResource(path = "by-sessionkey", rel = "find-by-session-key")
    Session findBySessionKey(String sessionKey);

    // @RestResource(path = "exists-for-user", rel = "exists-for-user")
    boolean existsAllBySessionTitleAndUsername(@Param("sessionTitle") String sessionTitle, @Param("username") String username);

    // @RestResource(path = "by-username", rel = "find-by-username")
    List<Session> findSessionByUsername(String username);

    // @RestResource(path = "prev-session-by-username", rel = "prev-session-by-username")
    @Query(value = "select new PrevSession(r.sessionKey, r.sessionTitle, r.username, r.updateTimestamp, r.workflow) from Session r WHERE r.username = :username ORDER BY r.updateTimestamp DESC")
    List<PrevSession> findSessionKeyByUsername(@Param("username") String username);
}
