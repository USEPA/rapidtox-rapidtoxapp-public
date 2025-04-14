package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HazardEffectClassifier implements HazardClassifier {

    private final String classification;
    private final Pattern effectPattern;

    HazardEffectClassifier(String respiratory, String effectPattern) {
        this.classification = respiratory;
        this.effectPattern = Pattern.compile(effectPattern, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public String getClassificationName() {
        return this.classification;
    }

    @Override
    public Optional<String> classificationFor(Hazard h) {
        return effectPattern.matcher(h.getEffect()).find() ? Optional.of(classification) : Optional.empty();
    }

}
