package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.bioactivity.model.Toxcast;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportBioactivityModel {

	String model;
	String receptor;
	String agonist;
	String antagonist;
	String binding;

	public static Mapper<Toxcast, ReportBioactivityModel> MAPPER = new Mapper<>() {
		@Override
		public ReportBioactivityModel map(Toxcast t) {
			return ReportBioactivityModel.builder()
					.model(t.getModel())
					.receptor(t.getReceptor())
					.agonist(t.getAgonist())
					.antagonist(t.getAntagonist())
					.binding(t.getBinding())
					.build();
		}
	};

	public static Mapper<Toxcast, ReportBioactivityModel> mapper() {
		return MAPPER;
	}
}
