package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.sessionreport.CustomHazardToHazardDtoMapper;
import gov.epa.ccte.api.rapidtox.sessionreport.HazardDTO;
import gov.epa.ccte.api.rapidtox.sessionreport.HazardToHazardDtoMapper;
import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HazardHydrationService {

    private final HazardRepository hazardRepo;
    private final CustomHazardToHazardDtoMapper customToHazardDtoMapper;
    private final HazardToHazardDtoMapper hazardDtoMapper;

    public List<HazardDTO> getSelectedHazardDtos(ReportRequest req) {
        if (req.getData() == null || req.getData().getHazard() == null) {
            return Collections.emptyList();
        }
        List<HazardDTO> selectedHazards = new ArrayList<>(hydrateHazardDtosBySelection(req.getData().getHazard()));
        List<HazardDTO> customSelectedHazards = filterSelectedCustomDataByRequestedDtxsid(req.getDtxsid(), req.getData().getCustomData(), req.getData().getHazard());
        selectedHazards.addAll(customSelectedHazards);
        return selectedHazards;
    }

    public List<HazardDTO> filterSelectedCustomDataByRequestedDtxsid(String dtxsid, List<CustomHazard> customData, Collection<Integer> selectedHazardIds) {
        return customData.stream()
                .filter(cd -> cd.getDtxsid().equalsIgnoreCase(dtxsid))
                .filter(cd -> selectedHazardIds.contains(cd.getId()))
                .map(customToHazardDtoMapper::map)
                .collect(Collectors.toList());
    }

    public List<HazardDTO> hydrateHazardDtosBySelection(List<Integer> hazardIds) {
        return hydrateHazardsBySelection(hazardIds).stream()
                .map(hazardDtoMapper::map)
                .toList();
    }

    private List<Hazard> hydrateHazardsBySelection(List<Integer> hazardIds) {
        return hazardRepo.findByIdInOrderByPreferredNameAscSuperCategoryAsc(hazardIds);
    }

}
