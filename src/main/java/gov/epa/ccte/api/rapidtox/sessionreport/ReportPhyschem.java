package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemSummary;
import static gov.epa.ccte.api.rapidtox.util.JasperHelper.*;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportPhyschem {

	String property;
	String unit;
	String expMean;
	String expMedian;
	String expRange;
	String predMean;
	String predMedian;
	String predRange;

	public static Mapper<PhyschemSummary, ReportPhyschem> MAPPER = new Mapper<>() {
		@Override
		public ReportPhyschem map(PhyschemSummary p) {
			return ReportPhyschem.builder()
					.property(p.getProperty())
					.unit(formatNullableString(p.getUnit()))
					.expMean(formatDoubleValue(p.getExpMean()))
					.expMedian(formatDoubleValue(p.getExpMedian()))
					.expRange(formatDoubleRangeValue(p.getExpMin(), p.getExpMax()))
					.predMean(formatDoubleValue(p.getPredMean()))
					.predMedian(formatDoubleValue(p.getPredMedian()))
					.predRange(formatDoubleRangeValue(p.getPredMin(), p.getPredMax()))
					.build();
		}

	};

	public static Mapper<PhyschemSummary, ReportPhyschem> mapper() {
		return MAPPER;
	}

}
