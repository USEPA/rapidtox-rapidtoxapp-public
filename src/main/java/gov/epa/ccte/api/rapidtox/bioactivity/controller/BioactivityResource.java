package gov.epa.ccte.api.rapidtox.bioactivity.controller;

import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivityBerSummary;
import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivitySummary;
import gov.epa.ccte.api.rapidtox.bioactivity.model.ClowderPlotInfo;
import gov.epa.ccte.api.rapidtox.bioactivity.repository.ClowderPlotInfoRepository;
import gov.epa.ccte.api.rapidtox.bioactivity.repository.InvitroDbSummaryRepository;
import gov.epa.ccte.api.rapidtox.bioactivity.service.BioactivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Slf4j
@RestController
@RequestMapping(value = "bioactivity-summary")
@RequiredArgsConstructor
public class BioactivityResource {

	@Value("${clowder.read-key}")
	private String readKey;

	private final BioactivityService bioactivityService;

	private final ClowderPlotInfoRepository clowderPlotInfoRepository;

	@PostMapping("/search/by-dtxsids")
	public List<BioactivitySummary> getBioactivitySummary(@RequestBody List<String> dtxsids) {
		log.debug("dtxsid={}", dtxsids);

		return bioactivityService.fetchBioactivitySummaryForDtxsids(dtxsids);
	}

	@PostMapping("/ber/search/by-dtxsids")
	public List<BioactivityBerSummary> getBioactivityBerSummary(@RequestBody List<String> dtxsids) {
		log.debug("dtxsid={}", dtxsids);

		return bioactivityService.fetchBioactivityBerSummariesForDtxsids(dtxsids);
	}

	@GetMapping("/plot/search/by-dtxsid/{dtxsid}")
	public List<ClowderPlotInfo> getClowderPlotInfo(@PathVariable("dtxsid") String dtxsid) {
		log.debug("dtxsid={}", dtxsid);

		List<ClowderPlotInfo> clowderPlotInfos = clowderPlotInfoRepository.findByDtxsid(dtxsid);

		for (ClowderPlotInfo clowderPlotInfo : clowderPlotInfos) {
			clowderPlotInfo.setDownloadUrl(clowderPlotInfo.getDownloadUrl().replace("{clowder_api_key}", readKey));
		}

		return clowderPlotInfos;
	}
}
