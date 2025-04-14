package gov.epa.ccte.api.rapidtox.chemical.controller;

import gov.epa.ccte.api.rapidtox.chemical.model.RecentSearch;
import gov.epa.ccte.api.rapidtox.chemical.repository.RecentSearchRepository;
import static gov.epa.ccte.api.rapidtox.util.JwtHelper.emailFor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * REST controller for getting the {@link RecentSearch}s.
 */
@Slf4j
@RestController
@RequestMapping("/recent-search")
public class RecentSearchesResource {

    private final RecentSearchRepository recentSearchRepository;

    public RecentSearchesResource(RecentSearchRepository recentSearchRepository) {
        this.recentSearchRepository = recentSearchRepository;
    }

    @GetMapping("/test")
    public String greeting() {
        return "recent-search";
    }

    @PostMapping
    public void saveRecentSearch(@RequestBody String searchText, JwtAuthenticationToken principal) {
        final String username = emailFor(principal);
        log.debug("user name = {}, search text={}", username, searchText);

        recentSearchRepository.save(new RecentSearch(username, searchText));
    }

    @GetMapping
    public List<RecentSearch> getRecentSearches(JwtAuthenticationToken principal) {
        final String username = emailFor(principal);
        log.debug("user name = {} ", username);

        List<RecentSearch> result = recentSearchRepository.findByUserNameEqualsOrderBySearchedOnDesc(username);

        log.debug("{} of searches found for user name = {}", result.size(), username);

        return result;
    }

    @GetMapping("/{id}")
    public Optional<RecentSearch> getRecentSearches(@PathVariable("id") Integer id, JwtAuthenticationToken principal) {
        final String username = emailFor(principal);
        log.debug("get recent search with id = {} for user= {}", id);

        final Optional<RecentSearch> rs = recentSearchRepository.findById(id);
        if (rs.isPresent() && rs.get().getUserName().equals(username)) {
            return rs;
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteRecentSearch(@PathVariable("id") Integer id, JwtAuthenticationToken principal) {
        final String username = emailFor(principal);
        log.debug("delete recent search with id = {} for user= {}", id);

        // BUG in versions of spring-boot-data-jpa not backported to 2.7.x
        // supposed to "fail" silently if an entity with the ID doesn't exist
        // workaround:
        final Optional<RecentSearch> rs = recentSearchRepository.findById(id);
        if (rs.isPresent() && rs.get().getUserName().equals(username)) {
            recentSearchRepository.delete(rs.get());
        }
    }

}
