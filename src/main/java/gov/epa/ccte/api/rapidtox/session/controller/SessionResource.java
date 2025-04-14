package gov.epa.ccte.api.rapidtox.session.controller;

import gov.epa.ccte.api.rapidtox.session.model.PrevSession;
import gov.epa.ccte.api.rapidtox.session.model.Session;
import gov.epa.ccte.api.rapidtox.session.service.SessionService;
import static gov.epa.ccte.api.rapidtox.util.JwtHelper.emailFor;
import java.time.Instant;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
@RestController
@RequestMapping(value = "/session-recall")
@RequiredArgsConstructor
public class SessionResource {

    private final SessionService sessionService;

    @GetMapping("/search/prev-session-by-username")
    public Collection<PrevSession> fetchPreviousSessions(JwtAuthenticationToken principal) {
        log.debug("fetchPreviousSessionsByUsername: {}", principal.getTokenAttributes().get("email"));
        return sessionService.findSessionKeyByUsername((String) principal.getTokenAttributes().get("email"));
    }

    @PostMapping
    public Session createSession(@RequestBody Session newSession, JwtAuthenticationToken principal) {
        log.debug("createSession: {}", newSession);
        newSession.setUsername(emailFor(principal)); // override the incoming value with actual logged in username
        return sessionService.create(newSession);
    }

    @GetMapping("/{sessionKey}")
    public Session fetchSession(@PathVariable("sessionKey") String sessionKey, JwtAuthenticationToken principal) {
        log.debug("findSessionBySessionKey: {}", sessionKey);
        Optional<Session> session = sessionService.findById(sessionKey);
        sessionService.validateSessionOwnedByPrincipal(session, principal);
        return session.get();
    }

    /* this call only allows the session title to change */
    @PatchMapping("/{sessionKey}")
    public Session renameSession(@PathVariable("sessionKey") String sessionKey, @RequestBody Session renameRequest, JwtAuthenticationToken principal) {
        log.debug("renameSession key: {}", sessionKey);
        log.debug("new session name: {}", renameRequest.getSessionTitle());

        Optional<Session> sessionToRename = sessionService.findById(sessionKey);
        sessionService.validateSessionOwnedByPrincipal(sessionToRename, principal);
        sessionToRename.get().setSessionTitle(renameRequest.getSessionTitle());
        sessionToRename.get().setUpdateTimestamp(Instant.now());
        return sessionService.update(sessionToRename.get());
    }

    /* this call allows data to change, but not the meta-data */
    @PutMapping("/{sessionKey}")
    public Session updateSession(@PathVariable("sessionKey") String sessionKey, @RequestBody Session updatedSession, JwtAuthenticationToken principal) {
        log.debug("patchSessionBySessionKey: {}", sessionKey);
        Optional<Session> originalSession = sessionService.findById(sessionKey);
        Session orig = originalSession.get();
        sessionService.validateSessionOwnedByPrincipal(originalSession, principal);
        fill(orig, updatedSession);
        return sessionService.update(updatedSession);
    }

    void fill(Session from, Session into) {
        into.setSessionKey(from.getSessionKey());
        into.setSessionTitle(from.getSessionTitle());
        into.setWorkflow(from.getWorkflow());
        into.setUsername(from.getUsername());
        into.setUpdateTimestamp(Instant.now());
        into.setCreateTimestamp(from.getCreateTimestamp());
    }

    @DeleteMapping("/{sessionKey}")
    public void deleteSession(@PathVariable("sessionKey") String sessionKey, JwtAuthenticationToken principal) {
        log.debug("deleteSessionBySessionKey: {} {}", sessionKey);
        Optional<Session> session = sessionService.findById(sessionKey);
        sessionService.validateSessionOwnedByPrincipal(session, principal);
        sessionService.delete(session.get());
    }

    @RestControllerAdvice
    static class SessionNotFoundAdvice {

        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String sessionNotFoundHandler(ResourceNotFoundException ex) {
            return ex.getMessage();
        }
    }

    @RestControllerAdvice
    static class SessionAccessForbiddenAdvice {

        @ExceptionHandler(AccessForbiddenException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        String accessForbiddenHandler(AccessForbiddenException ex) {
            return ex.getMessage();
        }
    }
}
