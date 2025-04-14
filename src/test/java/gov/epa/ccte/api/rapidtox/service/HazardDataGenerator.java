package gov.epa.ccte.api.rapidtox.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import java.math.BigDecimal;
import java.util.Random;

public class HazardDataGenerator {

	private static final String[] SUPER_CATEGORIES = {
		"Media Exposure Guidelines",
		"Toxicity Value",
		"Acute Exposure Guidelines",
		"Dose Response Summary Value",
		"Mortality Response Summary Value"
	};

	private static final String[] TOXVAL_TYPES = {
		"cancer slope factor (provisional)",
		"LOAEL",
		"LOEL",
		"BMC"
	};

	private static final String[] EXPOSURE_ROUTES = {
		"oal: diet; highest dose by gavage as a positive control",
		"dermal",
		"environmental",
		"implant",
		"infusion",
		"inhalation",
		"Inhalation",
		"inhalation: nose only",
		"injection"
	};

	private static final String[] EXPOSURE_METHODS = {
		"aerosol",
		"capsule",
		"capsule; gavage",
		"capsule; milk emulsions",
		"chamber",
		"choice",
		"diet"
	};

	private static final String[] STUDY_TYPES = {
		"acute",
		"chronic",
		"developmental",
		"reproduction developmental",
		"short-term",
		"subchronic",
		"toxicity value",
		"Toxicity Value",
		"uterotrophic"
	};

	private static final String[] STUDY_DURATION_UNITS = {
		"minutes",
		"hours",
		"days"
	};

	private static final String[] SPECIES = {
		"Cat",
		"Cow Family",
		"Dog",
		"European Rabbit",
		"Guinea Pig",
		"Hamster",
		"House Mouse",
		"Human",
		"Mink",
		"Monkey",
		"Mouse",
		"Mouse, Rat",
		"Pig",
		"Primate",
		"Rabbit",
		"Rat",
		"Rhesus Macaque",
		"Western European House Mouse"
	};
	private static final Random rng = new Random();

	static String newDTXSID(int id) {
		return String.format("DTXSID%06d", id);
	}

	private static String randomStringFrom(String[] strings) {
		return strings[rng.nextInt(strings.length)];
	}

	static Hazard randomHazard(String dtxsid) {
		return Hazard.builder()
				.dtxsid(dtxsid)
				.preferredName("preferred-name")
				.casrn("casrn")
				.superCategory(randomStringFrom(SUPER_CATEGORIES))
				.toxvalType(randomStringFrom(TOXVAL_TYPES))
				.toxvalSubtype("subtype")
				.toxvalNumeric(rng.nextDouble(1000))
				.toxvalTypDefn("toxval-type defintition")
				.toxvalUnits("units")
				.superSource("super-source")
				.source("source")
				.studyType(randomStringFrom(STUDY_TYPES))
				.studyDuration(rng.nextDouble(1000))
				.studyDurationUnits(randomStringFrom(STUDY_DURATION_UNITS))
				.effect("effect")
				.exposureMethod(randomStringFrom(EXPOSURE_METHODS))
				.exposureRoute(randomStringFrom(EXPOSURE_ROUTES))
				.riskAssessmentClass("risk-class")
				.speciesCommon(randomStringFrom(SPECIES))
				.build();
	}

}
