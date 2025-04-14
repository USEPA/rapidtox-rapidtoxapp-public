package gov.epa.ccte.api.rapidtox.bioactivity.controller;

import gov.epa.ccte.api.rapidtox.bioactivity.model.Toxcast;
import gov.epa.ccte.api.rapidtox.bioactivity.repository.ToxcastPodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "bioactivity")
public class ToxcastResource {

    private final ToxcastPodRepository toxcastPodRepository;

    public ToxcastResource(ToxcastPodRepository toxcastPodRepository) {
        this.toxcastPodRepository = toxcastPodRepository;
    }

    @PostMapping("/toxcast/by-dtxsids")
    public List<Toxcast> getBioactivitySummary(@RequestBody List<String> dtxsids) {
        log.debug("dtxsids={}", dtxsids);

        return toxcastPodRepository.findByDtxsidIn(dtxsids);
    }
}
