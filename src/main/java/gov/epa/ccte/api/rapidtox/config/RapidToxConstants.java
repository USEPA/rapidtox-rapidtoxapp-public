package gov.epa.ccte.api.rapidtox.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RapidToxConstants {

	private RapidToxConstants() {
		throw new UnsupportedOperationException("pure static class, don't instantiate");
	}

	public static final String TREAT_AS_POD = "Dose Response Summary Value";
	public static final String TREAT_AS_TOX = "Toxicity Value";
    private static final String CUSTOM_PREFIX = "Custom ";
	public static final String TREAT_AS_CUSTOM_POD = CUSTOM_PREFIX + TREAT_AS_POD;
	public static final String TREAT_AS_CUSTOM_TOX = CUSTOM_PREFIX + TREAT_AS_TOX;
    private static final String CONVERTED_PREFIX = "Converted ";
	public static final String TREAT_AS_CONVERTED_POD = CONVERTED_PREFIX + TREAT_AS_POD;
	public static final String TREAT_AS_CONVERTED_TOX = CONVERTED_PREFIX + TREAT_AS_TOX;

	public static final List<String> PHYSCHEM_PROPERTY_NAMES = List.of(
			"Henry's Law", 
			"Boiling Point",
			"Melting Point", 
			"Density",
			"Vapor Pressure", 
			"Water Solubility",
			"LogKoa: Octanol-Air", 
			"LogKow: Octanol-Water"
	);

	public static final List<String> ENVFATE_PROPERTY_NAMES = List.of(
			"Biodeg. Half-Life",
			"Soil Adsorp. Coeff. (Koc)"
	);

}
