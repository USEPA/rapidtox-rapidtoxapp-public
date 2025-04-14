package gov.epa.ccte.api.rapidtox.sessionreport;

import java.util.*;
import java.util.stream.Stream;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuperCategory {

	// change this list if 
	// (a) the ordering of the super categories changes, or
	// (b) the contents of the super categories changes, upstream
	// not sure if this belongs here or in the ReportGenerator class (could go either way)
	// this is NOT a data requirement, but a reporting requirement and a UI requirement,
	// so here seems to make more sense.
	// It may make sense to make this a service endpoint so that everyone (UI, report)
	// plays by the same book
	public static final String[] REQUIRED_ORDERED_SUPER_CATEGORIES_ARR = {
		"Toxicity Value",
		"Media Exposure Guidelines",
		"Acute Exposure Guidelines",
		"Mortality Response Summary Value",
		"Dose Response Summary Value"
	};

	public static final String[] ALSO_KNOWN_SUPER_CATEGORIES_ARR = {
		"Custom Toxicity Value",
		"Custom Dose Response Summary Value"
	};

	public static final List<String> REQUIRED_ORDERED_SUPER_CATEGORIES;

	public static final List<String> KNOWN_ORDERED_SUPER_CATEGORIES;

	public static final List<String> SUPER_CATEGORY_ORDER;

	static {
		REQUIRED_ORDERED_SUPER_CATEGORIES = List.of(REQUIRED_ORDERED_SUPER_CATEGORIES_ARR);
		List<String> build = new ArrayList<>();
		build.addAll(REQUIRED_ORDERED_SUPER_CATEGORIES);
        // add "Converted " + known
		Stream.of(ALSO_KNOWN_SUPER_CATEGORIES_ARR).forEach(i->build.add("Converted " + i));
        // add custom
		Stream.of(ALSO_KNOWN_SUPER_CATEGORIES_ARR).forEach(build::add);

		KNOWN_ORDERED_SUPER_CATEGORIES = Collections.unmodifiableList(build);

		SUPER_CATEGORY_ORDER = Collections.unmodifiableList(KNOWN_ORDERED_SUPER_CATEGORIES);
	}

	private String categoryName;
	private boolean availableData;

	public boolean hasAvailableData() {
		return availableData;
	}

}
