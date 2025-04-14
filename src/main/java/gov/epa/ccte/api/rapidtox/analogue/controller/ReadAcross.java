package gov.epa.ccte.api.rapidtox.analogue.controller;

import gov.epa.ccte.api.rapidtox.physchem.repository.PhyschemPredictedRepository;
import gov.epa.ccte.api.rapidtox.physchem.repository.SimilarityPhychemAggrRepositoryER;
import gov.epa.ccte.api.rapidtox.physchem.repository.SimilarityPhychemAggrRepository;
import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemPredicted;
import gov.epa.ccte.api.rapidtox.physchem.model.SimilarityPhychemAggr;
import gov.epa.ccte.api.rapidtox.physchem.model.SimilarityPhychemAggrER;
import gov.epa.ccte.api.rapidtox.adme.model.Adme;
import gov.epa.ccte.api.rapidtox.chemical.model.ChemicalSearch;
import gov.epa.ccte.api.rapidtox.adme.repository.AdmeRepository;
import gov.epa.ccte.api.rapidtox.chemical.repository.ChemicalSearchRepository;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "readacross")
public class ReadAcross {

	private final SimilarityPhychemAggrRepository similarityPhychemAggrRepository;

	private final SimilarityPhychemAggrRepositoryER similarityPhychemAggrRepositoryER;

	private final ChemicalSearchRepository chemicalSearchRepository;

	private final PhyschemPredictedRepository physchemPredictedRepository;

	private final AdmeRepository admeRepository;

	private final String[] TARGET_PROPS = {"Vapor Pressure", "Water Solubility", "LogKoa: Octanol-Air", "LogKow: Octanol-Water"};

	public ReadAcross(SimilarityPhychemAggrRepository similarityPhychemAggrRepository,
			SimilarityPhychemAggrRepositoryER similarityPhychemAggrRepositoryER,
			ChemicalSearchRepository chemicalSearchRepository,
			PhyschemPredictedRepository physchemPredictedRepository,
			AdmeRepository admeRepository) {
		this.similarityPhychemAggrRepository = similarityPhychemAggrRepository;
		this.similarityPhychemAggrRepositoryER = similarityPhychemAggrRepositoryER;
		this.chemicalSearchRepository = chemicalSearchRepository;
		this.physchemPredictedRepository = physchemPredictedRepository;
		this.admeRepository = admeRepository;
	}

	@PostMapping("/hha/by-dtxsids/{dtxsid}")
	public List<SimilarityPhychemAggr> getPhyschemSummary(@PathVariable("dtxsid") String dtxsid, @RequestBody List<String> dtxsids) {
		log.debug("dtxsid = {}", dtxsid);
		log.debug("dtxsids = {}", dtxsids);
		if (!isNullOrEmpty(dtxsid)) {
			dtxsids.add(dtxsid);
		}
		List<SimilarityPhychemAggr> response = similarityPhychemAggrRepository.findAllByDtxsidIn(dtxsids);
		if (!isNullOrEmpty(dtxsid)) {
			SimilarityPhychemAggr similarPhyschemAggr = response.stream()
					.filter(c -> c.getDtxsid().equals(dtxsid))
					.findFirst()
					.orElse(null);

			if (similarPhyschemAggr != null) {
				response.remove(similarPhyschemAggr);
				similarPhyschemAggr.setIsTarget(true);
				response.add(0, similarPhyschemAggr);
			} else {

				List<PhyschemPredicted> physchemPredicted = physchemPredictedRepository.findByDtxsidEqualsAndSourceEqualsAndPropertyInIgnoreCase(dtxsid, "OPERA", Arrays.asList(TARGET_PROPS));

				Adme adme = admeRepository.findAdmeByDtxsidEquals(dtxsid);

				SimilarityPhychemAggr similarityPhychemAggr = new SimilarityPhychemAggr();
				ChemicalSearch chemicalSearch = this.chemicalSearchRepository.findFirstByDtxsid(dtxsid);
				similarityPhychemAggr.setPreferredName(chemicalSearch.getPreferredName());
				similarityPhychemAggr.setDtxsid(dtxsid);
				similarityPhychemAggr.setHasStructureImage(1);
				if (physchemPredicted != null) {
					similarityPhychemAggr.setPhychemWaterSolubility(physchemPredicted.stream()
							.filter(prop -> "Water Solubility".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream()
							.filter(prop -> "Water Solubility".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
					similarityPhychemAggr.setPhychemVp(physchemPredicted.stream()
							.filter(prop -> "Vapor Pressure".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream()
							.filter(prop -> "Vapor Pressure".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
					similarityPhychemAggr.setPhychemLogkoa(physchemPredicted.stream().filter(
							prop -> "LogKoa: Octanol-Air".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream().filter(
							prop -> "LogKoa: Octanol-Air".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
					similarityPhychemAggr.setPhychemLogkow(physchemPredicted.stream().filter(
							prop -> "LogKow: Octanol-Water".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream().filter(
							prop -> "LogKow: Octanol-Water".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
				}
				if (adme != null) {
					similarityPhychemAggr.setAdmeFuhp(adme.getAdmeFuhp().toString());
					similarityPhychemAggr.setAdmeVolOfDist(adme.getAdmeVolOfDist());
					similarityPhychemAggr.setAdmeHsspc(adme.getAdmeHsspc());
					similarityPhychemAggr.setAdmePkHalflife(adme.getAdmePkHalflife());
					similarityPhychemAggr.setAdmeInvitroHc(adme.getAdmeInvitroHc());
				}
				similarityPhychemAggr.setIsTarget(true);
				response.add(0, similarityPhychemAggr);
			}
		}

		return response;
	}

	public static boolean isNullOrEmpty(String dtxsid) {
		return dtxsid == null || dtxsid.equals("null") || dtxsid.isEmpty();
	}

	@PostMapping("/er/by-dtxsids/{dtxsid}")
	public List<SimilarityPhychemAggrER> getPhyschemERSummary(@PathVariable("dtxsid") String dtxsid, @RequestBody List<String> dtxsids) {
		log.debug("dtxsid = {}", dtxsid);
		log.debug("dtxsids = {}", dtxsids);
		dtxsids.add(dtxsid);
		List<SimilarityPhychemAggrER> response = similarityPhychemAggrRepositoryER.findAllByDtxsidIn(dtxsids);
		if (dtxsid != null && !dtxsid.equals("")) {
			SimilarityPhychemAggrER similarPhyschemAggr = response.stream()
					.filter(c -> c.getDtxsid().equals(dtxsid))
					.findFirst()
					.orElse(null);

			if (similarPhyschemAggr != null) {
				response.remove(similarPhyschemAggr);
				similarPhyschemAggr.setIsTarget(true);
				response.add(0, similarPhyschemAggr);
			} else {

				List<PhyschemPredicted> physchemPredicted = physchemPredictedRepository.findByDtxsidEqualsAndSourceEqualsAndPropertyInIgnoreCase(dtxsid, "OPERA", Arrays.asList(TARGET_PROPS));

				Adme adme = admeRepository.findAdmeByDtxsidEquals(dtxsid);

				SimilarityPhychemAggrER similarityPhychemAggr = new SimilarityPhychemAggrER();
				ChemicalSearch chemicalSearch = this.chemicalSearchRepository.findFirstByDtxsid(dtxsid);
				similarityPhychemAggr.setPreferredName(chemicalSearch.getPreferredName());
				similarityPhychemAggr.setDtxsid(dtxsid);
				similarityPhychemAggr.setHasStructureImage(1);
				if (physchemPredicted != null) {
					similarityPhychemAggr.setPhychemWaterSolubility(physchemPredicted.stream()
							.filter(prop -> "Water Solubility".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream()
							.filter(prop -> "Water Solubility".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
					similarityPhychemAggr.setPhychemVp(physchemPredicted.stream()
							.filter(prop -> "Vapor Pressure".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream()
							.filter(prop -> "Vapor Pressure".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
					similarityPhychemAggr.setPhychemLogkoa(physchemPredicted.stream().filter(
							prop -> "LogKoa: Octanol-Air".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream().filter(
							prop -> "LogKoa: Octanol-Air".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
					similarityPhychemAggr.setPhychemLogkow(physchemPredicted.stream().filter(
							prop -> "LogKow: Octanol-Water".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null) != null ? physchemPredicted.stream().filter(
							prop -> "LogKow: Octanol-Water".equalsIgnoreCase(prop.getProperty()))
							.findAny().orElse(null).getResult() : null);
				}
				if (adme != null) {
					similarityPhychemAggr.setAdmeFuhp(adme.getAdmeFuhp().toString());
					similarityPhychemAggr.setAdmeVolOfDist(adme.getAdmeVolOfDist());
					similarityPhychemAggr.setAdmeHsspc(adme.getAdmeHsspc());
					similarityPhychemAggr.setAdmePkHalflife(adme.getAdmePkHalflife());
					similarityPhychemAggr.setAdmeInvitroHc(adme.getAdmeInvitroHc());
				}
				similarityPhychemAggr.setIsTarget(true);
				response.add(0, similarityPhychemAggr);
			}
		}

		return response;
	}
}
