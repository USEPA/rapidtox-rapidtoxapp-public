package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.chemical.model.OdorThreshold;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportOdorThreshold {

	String dtxsid;
	String odor;
	String odorThreshold;

	public static Mapper<OdorThreshold, ReportOdorThreshold> MAPPER = new Mapper<>() {
		@Override
		public ReportOdorThreshold map(OdorThreshold o) {
			return ReportOdorThreshold.builder()
					.dtxsid(o.getDtxsid())
					.odor(o.getOdor())
					.odorThreshold(o.getOdorThreshold())
					.build();
		}

	};

	public static Mapper<OdorThreshold, ReportOdorThreshold> mapper() {
		return MAPPER;
	}

}
