package gov.epa.ccte.api.rapidtox.session.service;

import gov.epa.ccte.api.rapidtox.session.model.PrevSession;
import gov.epa.ccte.api.rapidtox.session.model.Session;
import gov.epa.ccte.api.rapidtox.session.repository.SessionRepository;
import static gov.epa.ccte.api.rapidtox.util.JwtHelper.emailFor;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepo;
    
    public void validateSessionOwnedByPrincipal(String sessionKey, JwtAuthenticationToken principal) {
        Assert.notNull(sessionKey, "sessionKey cannot be null");
        Assert.isTrue(!sessionKey.isBlank(), "session key cannot be blank");
        validateSessionOwnedByPrincipal(sessionRepo.findById(sessionKey), principal);
    }

    public void validateSessionOwnedByPrincipal(Optional<Session> session, JwtAuthenticationToken principal) {
        Assert.notNull(session, "session cannot be null");
        Assert.isTrue(session.isPresent(), "session must be present");
        Assert.notNull(principal, "principal cannot be null");
        String email = emailFor(principal);
        Assert.notNull(email, "email must be provided");
        Assert.isTrue(session.get().getUsername().equals(email), "session username and logged in username differ");
    }
    
    public void delete(Session session) {
        sessionRepo.delete(session);
    }

    public Session update(Session updatedSession) {
        return sessionRepo.save(updatedSession);
    }

    public Session create(Session newSession) {
        return sessionRepo.save(newSession);
    }

    public Session findBySessionKey(String originalSessionKey) {
        return sessionRepo.findBySessionKey(originalSessionKey);
    }

    public boolean existsAllBySessionTitleAndUsername(String sessionTitle, String username) {
        return sessionRepo.existsAllBySessionTitleAndUsername(sessionTitle, username);
    }

    public Collection<PrevSession> findSessionKeyByUsername(String username) {
        return sessionRepo.findSessionKeyByUsername(username);
    }

    public Optional<Session> findById(String sessionKey) {
        return sessionRepo.findById(sessionKey);
    }

}
