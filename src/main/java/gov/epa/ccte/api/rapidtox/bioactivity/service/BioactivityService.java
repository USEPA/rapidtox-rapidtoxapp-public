package gov.epa.ccte.api.rapidtox.bioactivity.service;

import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivityBerSummary;
import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivitySummary;
import gov.epa.ccte.api.rapidtox.bioactivity.model.InvitroDbSummary;
import gov.epa.ccte.api.rapidtox.bioactivity.repository.InvitroDbSummaryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BioactivityService {

    private final ModelMapper mapper;
    private final InvitroDbSummaryRepository invitroDbRepo;

    public List<BioactivitySummary> fetchBioactivitySummaryForDtxsids(List<String> dtxsids) {
        return invitroDbRepo.findAllByDtxsidIn(dtxsids).stream()
                .map(i -> mapper.map(i, BioactivitySummary.class))
                .collect(Collectors.toList());
    }

    public List<BioactivityBerSummary> fetchBioactivityBerSummariesForDtxsids(List<String> dtxsids) {
        return invitroDbRepo.findAllByDtxsidIn(dtxsids).stream()
                .map(i -> mapper.map(i, BioactivityBerSummary.class))
                .collect(Collectors.toList());
    }

    public Optional<BioactivitySummary> fetchBioactivitySummaryForDtxsid(String dtxsid) {
        return invitroDbRepo.findByDtxsid(dtxsid).map(i -> mapper.map(i, BioactivitySummary.class));
    }

}
