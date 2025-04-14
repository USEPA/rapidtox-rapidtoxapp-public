package gov.epa.ccte.api.rapidtox.service;

import gov.epa.ccte.api.rapidtox.hazard.service.HazardClassificationService;
import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import java.util.Set;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

public class HazardCategorizerTests {

		private final String effectString = "immuno";
		private final Hazard h = Hazard.builder().toxvalType("").effect(effectString).build();

		private final HazardClassificationService svc = new HazardClassificationService();

		@Test
		void programLoads() {
				assertThat(svc).isNotNull();
		}

		@Test
		void testForImmuno() {
				final Set<String> results = svc.classify(h);
				assertThat(results.contains("immunotoxicity"));
				System.out.println(results);
		}
		
}
