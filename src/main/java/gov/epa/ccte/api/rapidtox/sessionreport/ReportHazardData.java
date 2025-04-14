package gov.epa.ccte.api.rapidtox.sessionreport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportHazardData {

	List<ReportHazard> hazards;
	List<ReportHazard> selectedHazards;
	List<SuperCategory> superCategories;

	public static ReportHazardData build(List<ReportHazard> hazards) {
		return ReportHazardData.builder()
				.hazards(hazards)
				.superCategories(buildSuperCategories(hazards, hazards))
				.build();
	}

	public static ReportHazardData build(List<ReportHazard> hazards, List<ReportHazard> selectedHazards) {
		return ReportHazardData.builder()
				.hazards(hazards)
				.selectedHazards(selectedHazards)
				.superCategories(buildSuperCategories(hazards, selectedHazards))
				.build();
	}
	
	public static List<SuperCategory> buildSuperCategories(List<ReportHazard> hazards, List<ReportHazard> selectedHazards) {
		// get the list of required categories
		List<String> requiredCategories = new ArrayList<>(SuperCategory.REQUIRED_ORDERED_SUPER_CATEGORIES);
		// get a list of categories available in the data
		List<String> availableCategories = hazards.stream().map(h -> h.getSuperCategory()).distinct().collect(Collectors.toList());
		// and collect those that are in the selected data, too
		selectedHazards.stream().map(h -> h.getSuperCategory()).forEach(s -> {
			if (!availableCategories.contains(s)) {
				availableCategories.add(s);
			}
		});
		// accumulate categories
		List<SuperCategory> allScs = new ArrayList<>();
		// gather all the required ones
		for (String sc : requiredCategories) {
			SuperCategory sco = SuperCategory.builder().categoryName(sc).availableData(availableCategories.contains(sc)).build();
			availableCategories.remove(sc);
			allScs.add(sco);
		}
		// gather remaining ones
		for (String sc : availableCategories) {
			SuperCategory sco = SuperCategory.builder().categoryName(sc).availableData(availableCategories.contains(sc)).build();
			allScs.add(sco);
		}
		return allScs;
	}
}
