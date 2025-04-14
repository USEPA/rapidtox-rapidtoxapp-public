package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.util.JasperHelper;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class HazardDTO {

    private Integer id;
    private String dtxsid;
    private String casrn;
    private String preferredName;
    private Double molecularWeight;
    private String toxvalType;
    private String toxvalSubtype;
    private String riskAssessmentClass;
    private Double toxvalNumeric;
    private String toxvalUnits;
    private String studyType;
    private Double studyDuration;
    private String studyDurationUnits;
    private String exposureRoute;
    private String exposureMethod;
    private String species;
    private String source;
    private String effect;
    private String toxvalTypDefn;
    private OffsetDateTime createTimestamp;
    private String superCategory;
    private String sourceUrl;
    private String ssourceUrl;
    private String clowderId;
    private String superSource;
    private String workflow;
    private Double originalValue;
    private String originalUnits;

    public String getFormattedDuration() {
        String str = JasperHelper.formatDoubleValue(this.getStudyDuration());
        return str;
    }

    public String getFormattedMolecularWeight() {
        String str = JasperHelper.formatDoubleValue(this.getMolecularWeight());
        return str;
    }

    public String getFormattedValue() {
        String str = JasperHelper.formatDoubleValue(this.getToxvalNumeric());
        if (this.originalValue != null) {
            str += " (" + JasperHelper.formatDoubleValue(this.originalValue) + ")";
        }
        return str;
    }

    public String getFormattedUnits() {
        String str = this.getToxvalUnits();
        if (originalUnits != null) {
            str += " (" + originalUnits + ")";
        }
        return str;
    }

}
