package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class HazardClassificationService {

    private static final String DEVELOP_EFFECT_PATTERN = "dev|(pup)|fetal|malform|neonate|pup|ossification|delayed|fontanel|supernumary|postnatal|PND";
    private static final String REPRO_EFFECT_PATTERN = "repro|estrous|litter|ovary|cervi|epididymis|implantation|ova|resorption|seminiferous|sperm|testes|testic|testis|utero|uterus|vagin|maternal|sexual";
    private static final String IMMUNO_EFFECT_PATTERN = "immun|neutroph|cytokine|lymph|antibody|b cell|basophil|cd3\\+|cd4\\+|cd8\\+|cytokine|eosinophil|ige|igm|il\\-1|il\\-4|immun|inflam|leukocyte|lymphocyte|monocyte|NK cell|spleen|t cell|thymus|tnf\\-alpha|wbc";
    private static final String NEURO_EFFECT_PATTERN = "brain|neuro|behavior|acetylcholinesterase|cerebell|cerebr|CNS|convulsion|locomotor|nerv|reflex|seizure|locomotion|motor";
    private static final String ENDOCRINE_EFFECT_PATTERN = "adrenal|hormone|androgen|estrogen|thyroid|insulin|T4|testosteron|thyroid|thyroxine|triiodothyronine";
    private static final String RESP_EFFECT_PATTERN = "dyspnea|lung|respirat|thorax";

    private static final List<HazardEffectClassifier> CLASSIFIERS = List.of(
            new HazardEffectClassifier("developmental", DEVELOP_EFFECT_PATTERN),
            new HazardEffectClassifier("reproductive", REPRO_EFFECT_PATTERN),
            new HazardEffectClassifier("immunotoxicity", IMMUNO_EFFECT_PATTERN),
            new HazardEffectClassifier("neurotoxicity", NEURO_EFFECT_PATTERN),
            new HazardEffectClassifier("endocrine", ENDOCRINE_EFFECT_PATTERN),
            new HazardEffectClassifier("respiratory", RESP_EFFECT_PATTERN)
    );

    public Map<Integer, Set<String>> classify(Collection<Hazard> hazards) {
        Map<Integer, Set<String>> rv = new LinkedHashMap<>();
        for (Hazard h : hazards) {
            Set<String> classifications = classify(h);
            rv.put(h.getId(), classifications);
        }
        return rv;
    }

    public Set<String> classify(Hazard h) {
        // force consistent ordering of results
        Set<String> classifications = new LinkedHashSet<>();
        CLASSIFIERS.stream()
                .map(c -> c.classificationFor(h))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct()
                .forEach(classifications::add);
        return classifications;
    }

}
