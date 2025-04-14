package gov.epa.ccte.api.rapidtox.landscapereport;

import gov.epa.ccte.api.rapidtox.sessionreport.ReportHazardData;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportPhyschemData;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportOdorThresholdData;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportBioactivityData;
import java.awt.Image;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LandscapeReportData {
	String dtxsid;
	String workflow;
	String casrn;
	String preferredName;
	String ccdLink;
	Image structureImage;
	ReportPhyschemData physchemData;
	ReportHazardData hazardData;
	ReportOdorThresholdData odorThresholdData;
	ReportBioactivityData bioactivityData;
	
}
