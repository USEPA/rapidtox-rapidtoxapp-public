
package gov.epa.ccte.api.rapidtox.sessionreport;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportBioactivityData {
	List<ReportBioactivitySummary> activitySummary;
	List<ReportBioactivityBERSummary> berSummary;
	List<ReportBioactivityModel> models;
}
