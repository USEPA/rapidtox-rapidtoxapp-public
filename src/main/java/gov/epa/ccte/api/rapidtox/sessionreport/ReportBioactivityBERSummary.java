package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.bioactivity.model.BioactivityBerSummary;
import static gov.epa.ccte.api.rapidtox.util.JasperHelper.formatDoubleValue;
import static gov.epa.ccte.api.rapidtox.util.JasperHelper.formatNullableString;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportBioactivityBERSummary {

	String inactiveCount;
	String activeCount;
	String totalAssaysScreened;
	String hitRate;
	String aed95pctile05;
	String aed95pctile50;
	String aed95pctile95;
	String aed50pctile05;
	String aed50pctile50;
	String aed50pctile95;
	String aedUnit;
	String berLowerBound;
	String berMedian;
	String modelParam;
	String invitroVersion;

	public static Mapper<BioactivityBerSummary, ReportBioactivityBERSummary> MAPPER = new Mapper<>() {
		@Override
		public ReportBioactivityBERSummary map(BioactivityBerSummary s) {
			return ReportBioactivityBERSummary.builder()
					.activeCount(String.valueOf(s.getActiveCount()))
					.inactiveCount(String.valueOf(s.getInactiveCount()))
					.totalAssaysScreened(String.valueOf(s.getTotalAssaysScreened()))
					.hitRate(formatDoubleValue(s.getHitRate()))
					.aed95pctile05(formatDoubleValue(s.getAed95Pctile05()))
					.aed95pctile50(formatDoubleValue(s.getAed95Pctile50()))
					.aed95pctile95(formatDoubleValue(s.getAed95Pctile95()))
					.aed50pctile05(formatDoubleValue(s.getAed50Pctile05()))
					.aed50pctile50(formatDoubleValue(s.getAed50Pctile50()))
					.aed50pctile95(formatDoubleValue(s.getAed50Pctile95()))
					.aedUnit(formatNullableString(s.getAedUnit()))
					.berLowerBound(formatDoubleValue(s.getBerLowerbound()))
					.berMedian(formatDoubleValue(s.getBerMedian()))
					.modelParam(formatNullableString(s.getModelParam()))
					.invitroVersion(s.getInvitroVersion())
					.build();
		}
	};

	public static Mapper<BioactivityBerSummary, ReportBioactivityBERSummary> mapper() {
		return MAPPER;
	}
}
