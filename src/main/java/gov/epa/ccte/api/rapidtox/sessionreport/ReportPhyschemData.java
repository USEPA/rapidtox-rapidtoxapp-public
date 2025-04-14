package gov.epa.ccte.api.rapidtox.sessionreport;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportPhyschemData {

	List<ReportPhyschem> physchems;
	List<ReportPhyschem> envFates;
}
