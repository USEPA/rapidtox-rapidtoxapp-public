package gov.epa.ccte.api.rapidtox.analogue.controller;

import gov.epa.ccte.api.rapidtox.hazard.model.SimilarPodAggregatorER;
import gov.epa.ccte.api.rapidtox.hazard.model.SimilarPodAggregator;
import gov.epa.ccte.api.rapidtox.chemical.model.ChemicalSearch;
import gov.epa.ccte.api.rapidtox.chemical.repository.ChemicalSearchRepository;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "analogue")
public class AnalogueResource {

	@Autowired
	@Qualifier("similarityTemplate")
	private NamedParameterJdbcTemplate jdbcSimilarityTemplate;

	private final ChemicalSearchRepository chemicalSearchRepository;

	public AnalogueResource(ChemicalSearchRepository chemicalSearchRepository) {
		this.chemicalSearchRepository = chemicalSearchRepository;
	}

	@GetMapping("/test")
	public String greeting() {
		return "analogue";
	}

	@GetMapping("/hha/by-similarity/{dtxsid}/{tanimoto}/")
	@ResponseBody
	public List<SimilarPodAggregator> getSimilarPodsWithTanimoto(@PathVariable("dtxsid") String dtxsid, @PathVariable("tanimoto") Double tanimoto,
			@RequestParam("smiles") String smiles) throws UnsupportedEncodingException {
		return findSimilarHhaPods(smiles, tanimoto, dtxsid);
	}

	@GetMapping("/hha/by-similarity")
	@ResponseBody
	public List<SimilarPodAggregator> getSimHhaPods(
			@RequestParam(name = "dtxsid", required = false) String dtxsid,
			@RequestParam(name = "tanimoto", required = false) Double tanimoto,
			@RequestParam("smiles") String smiles) throws UnsupportedEncodingException, DataAccessException {
		if (tanimoto == null) {
			log.info("null tanimoto");
		}
		if (dtxsid == null) {
			log.info("null dtxsid");
		}
		log.info("smiles= {}, tanimoto= {}, dtxsid= \"{}\"", smiles, tanimoto, dtxsid);

		return findSimilarHhaPods(smiles, tanimoto, dtxsid);
	}

	public List<SimilarPodAggregator> findSimilarHhaPods(String smiles, Double tanimoto, String dtxsid) throws UnsupportedEncodingException, DataAccessException {
		String smilesDecoded = URLDecoder.decode(smiles, StandardCharsets.UTF_8.name());

		log.info("smiles= {}, tanimoto= {}, dtxsid= \"{}\"", smilesDecoded, tanimoto, dtxsid);

		// Setting up default value
		if (tanimoto == null) {
			tanimoto = 0.6;
		}
		// In order to implement equal - RAPIDTOX-572
		tanimoto = tanimoto - 0.001;

		String sql = "select 'false' as selected, cd.preferred_name, cd.casrn, c.dtxsid, c.dtxcid, c.iris_total, c.pprtv_total, c.atsdr_total, c.opp_total, c.other_total, "
				+ "       cast (bingo.getsimilarity(molfile,:smiles,'Tanimoto') as decimal) as similarity\n"
				+ "from similarity_search.dsstox_structures s join similarity_search.rapidtox_hazard_similarity_pod_aggr c on s.dsstox_compound_id = c.dtxcid "
				+ "join similarity_search.chemical_details cd on s.dsstox_compound_id = cd.dtxcid\n"
				+ "where (molfile @ (:tanimoto,1,:smiles,'Tanimoto')::bingo.sim)  AND c.dtxsid <> 'DTXSID00000000' ORDER BY similarity DESC";

		List<SimilarPodAggregator> similarPods = jdbcSimilarityTemplate.query(sql, new MapSqlParameterSource()
				.addValue("smiles", smilesDecoded)
				.addValue("tanimoto", tanimoto), new BeanPropertyRowMapper<>(SimilarPodAggregator.class)
		);

		if (dtxsid != null && !dtxsid.isBlank()) {
			SimilarPodAggregator similarPod = similarPods.stream()
					.filter(c -> c.getDtxsid().equals(dtxsid))
					.findFirst()
					.orElse(null);

			if (similarPod != null) {
				similarPods.remove(similarPod);
				similarPod.setIsTarget(true);
				similarPods.add(0, similarPod);
			} else {
				SimilarPodAggregator similarPodTarget = new SimilarPodAggregator();
				ChemicalSearch chemicalSearch = this.chemicalSearchRepository.findFirstByDtxsid(dtxsid);
				similarPodTarget.setPreferredName(chemicalSearch.getPreferredName());
				similarPodTarget.setCasrn(chemicalSearch.getCarsn());
				similarPodTarget.setDtxsid(dtxsid);
				similarPodTarget.setSimilarity(1.0);
				similarPodTarget.setAtsdrTotal(0);
				similarPodTarget.setPprtvTotal(0);
				similarPodTarget.setIrisTotal(0);
				similarPodTarget.setOppTotal(0);
				similarPodTarget.setOtherTotal(0);
				similarPodTarget.setIsTarget(true);
				similarPods.add(0, similarPodTarget);
			}
		}

		return similarPods;
	}

	@GetMapping("/er/by-similarity/{dtxsid}/{tanimoto}/")
	@ResponseBody
	public List<SimilarPodAggregatorER> getSimilarPodsERWithTanimoto(
			@PathVariable("dtxsid") String dtxsid,
			@PathVariable("tanimoto") Double tanimoto,
			@RequestParam("smiles") String smiles) throws UnsupportedEncodingException {
		return findSimilarErPods(smiles, tanimoto, dtxsid);
	}

	public List<SimilarPodAggregatorER> findSimilarErPods(String smiles, Double tanimoto, String dtxsid) throws DataAccessException, UnsupportedEncodingException {
		String smilesDecoded = URLDecoder.decode(smiles, StandardCharsets.UTF_8.name());

		log.debug("smiles= {}, tanimoto= {}", smilesDecoded, tanimoto);

		// Setting up default value
		if (tanimoto == null) {
			tanimoto = 0.6;
		}
		// In order to implement equal - RAPIDTOX-572
		tanimoto = tanimoto - 0.001;

		String sql = "select 'false' as selected, cd.preferred_name, cd.casrn, c.dtxsid, c.dtxcid, c.atsdr_total, c.calepa_total ,c.doe_total, c.other_total, "
				+ "       cast (bingo.getsimilarity(molfile,:smiles,'Tanimoto') as decimal) as similarity\n"
				+ "from similarity_search.dsstox_structures s join similarity_search.rapidtox_hazard_similarity_pod_aggr_er c on s.dsstox_compound_id = c.dtxcid "
				+ "join similarity_search.chemical_details cd on s.dsstox_compound_id = cd.dtxcid\n"
				+ "where (molfile @ (:tanimoto,1,:smiles,'Tanimoto')::bingo.sim)  AND c.dtxsid <> 'DTXSID00000000' ORDER BY similarity DESC";

		List<SimilarPodAggregatorER> similarPods = jdbcSimilarityTemplate.query(sql, new MapSqlParameterSource()
				.addValue("smiles", smilesDecoded)
				.addValue("tanimoto", tanimoto), new BeanPropertyRowMapper<>(SimilarPodAggregatorER.class)
		);

		if (dtxsid != null && dtxsid != "") {
			SimilarPodAggregatorER similarPod = similarPods.stream()
					.filter(c -> c.getDtxsid().equals(dtxsid))
					.findFirst()
					.orElse(null);

			if (similarPod != null) {
				similarPods.remove(similarPod);
				similarPod.setIsTarget(true);
				similarPods.add(0, similarPod);
			} else {
				SimilarPodAggregatorER similarPodTarget = new SimilarPodAggregatorER();
				ChemicalSearch chemicalSearch = this.chemicalSearchRepository.findFirstByDtxsid(dtxsid);
				similarPodTarget.setPreferredName(chemicalSearch.getPreferredName());
				similarPodTarget.setCasrn(chemicalSearch.getCarsn());
				similarPodTarget.setDtxsid(dtxsid);
				similarPodTarget.setSimilarity(1.0);
				similarPodTarget.setAtsdrTotal(0);
				similarPodTarget.setCalepaTotal(0);
				similarPodTarget.setDoeTotal(0);
				similarPodTarget.setOtherTotal(0);
				similarPodTarget.setIsTarget(true);
				similarPods.add(0, similarPodTarget);
			}
		}

		return similarPods;
	}
}
