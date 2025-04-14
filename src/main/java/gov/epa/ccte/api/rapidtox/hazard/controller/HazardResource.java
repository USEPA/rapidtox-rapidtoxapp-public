package gov.epa.ccte.api.rapidtox.hazard.controller;


import static gov.epa.ccte.api.rapidtox.config.RapidToxConstants.TREAT_AS_POD;
import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for getting the {@link gov.epa.ccte.api.rapidtox.domain}s.
 */
@Slf4j
@RestController
@RequestMapping(value = "hazard")
public class HazardResource {

    private final HazardRepository hazardRepository;

    public HazardResource(HazardRepository hazardRepository) {
        this.hazardRepository = hazardRepository;
    }

    @GetMapping("test")
    public String greeting(){
        return "Hello world";
    }

    @GetMapping("pod/{dtxsid}")
    public List<Hazard> getHazardPod(@PathVariable("dtxsid") String dtxsid){
        log.debug("dtxsid={}", dtxsid);

        return hazardRepository.findByDtxsidAndSuperCategoryEquals(dtxsid, TREAT_AS_POD);
    }


    @PostMapping("/")
    public List<Hazard> getHazardToxPod(@RequestBody List<String> dtxsids){
        log.debug("dtxsids = {}", dtxsids);

        List<Hazard> hazards = hazardRepository.findByDtxsidInOrderByPreferredNameAscSuperCategoryAsc(dtxsids);

        log.debug("{} records found for hazard", hazards.size());

        return hazards;
    }

    @PostMapping("/ids")
    public List<Hazard> getHazardToxPodByIds(@RequestBody List<Integer> ids){
        log.debug("dtxsids = {}", ids);

        List<Hazard> hazards = hazardRepository.findByIdInOrderByPreferredNameAscSuperCategoryAsc(ids);

        log.debug("{} records found for hazard", hazards.size());

        return hazards;
    }
}
