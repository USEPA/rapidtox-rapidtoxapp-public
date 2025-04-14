package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivitySummary;
import static gov.epa.ccte.api.rapidtox.util.JasperHelper.formatDoubleValue;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReportBioactivitySummary {

	String inactiveCount;
	String activeCount;
	String totalAssaysScreened;
	String hitRate;
	String invitroPod05;
	String aed50pctile05;
	String aed50pctile95;
	String invitroVersion;

	public static Mapper<BioactivitySummary, ReportBioactivitySummary> MAPPER = new Mapper<>() {
		@Override
		public ReportBioactivitySummary map(BioactivitySummary b) {
			return ReportBioactivitySummary.builder()
					.inactiveCount(String.valueOf(b.getInactiveCount()))
					.activeCount(String.valueOf(b.getActiveCount()))
					.totalAssaysScreened(String.valueOf(b.getTotalAssaysScreened()))
					.hitRate(formatDoubleValue(b.getHitRate()))
					.aed50pctile05(formatDoubleValue(b.getAed50Pctile05()))
					.aed50pctile95(formatDoubleValue(b.getAed95Pctile05()))
					.invitroPod05(formatDoubleValue(b.getInvitroPod05()))
					.invitroVersion(b.getInvitroVersion())
					.build();
		}

	};

	public static Mapper<BioactivitySummary, ReportBioactivitySummary> mapper() {
		return MAPPER;
	}

}
