package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import java.util.Optional;

public interface HazardClassifier {

    Optional<String> classificationFor(Hazard h);

    String getClassificationName();

}
