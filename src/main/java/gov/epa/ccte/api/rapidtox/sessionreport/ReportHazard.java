package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.util.JasperHelper;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportHazard {

    String dtxsid;
    String superCategory;
    String studyType;
    String studyDuration;
    String studyDurationUnits;
    String species;
    String type;
    String subType;
    String value;
    String units;
    String exposureRoute;
    String exposureMethod;
    String effect;
    String source;

    public static Mapper<Hazard, ReportHazard> hazardMapper() {
        return HAZARD_MAPPER;
    }
    
    public static Mapper<HazardDTO, ReportHazard> hazardDtoMapper() {
        return HAZARDDTO_MAPPER;
    }

    public static Mapper<Hazard, ReportHazard> HAZARD_MAPPER = new Mapper<>() {
        @Override
        public ReportHazard map(Hazard h) {
            return ReportHazard.builder()
                    .dtxsid(h.getDtxsid())
                    .superCategory(h.getSuperCategory())
                    .studyType(h.getStudyType())
                    .studyDuration(JasperHelper.formatDoubleValue(h.getStudyDuration()))
                    .studyDurationUnits(h.getStudyDurationUnits())
                    .species(h.getSpeciesCommon())
                    .type(h.getToxvalType())
                    .subType(h.getToxvalSubtype())
                    .value(JasperHelper.formatDoubleValue(h.getToxvalNumeric()))
                    .units(h.getToxvalUnits())
                    .exposureRoute(h.getExposureRoute())
                    .exposureMethod(h.getExposureMethod())
                    .effect(h.getEffect())
                    .source(h.getSource() != null ? h.getSource() : h.getSuperSource())
                    .build();
        }
    };
    public static Mapper<HazardDTO, ReportHazard> HAZARDDTO_MAPPER = new Mapper<>() {
        @Override
        public ReportHazard map(HazardDTO h) {
            return ReportHazard.builder()
                    .dtxsid(h.getDtxsid())
                    .superCategory(h.getSuperCategory())
                    .studyType(h.getStudyType())
                    .studyDuration(h.getFormattedDuration())
                    .studyDurationUnits(h.getStudyDurationUnits())
                    .species(h.getSpecies())
                    .type(h.getToxvalType())
                    .subType(h.getToxvalSubtype())
                    .value(h.getFormattedValue())
                    .units(h.getFormattedUnits())
                    .exposureRoute(h.getExposureRoute())
                    .exposureMethod(h.getExposureMethod())
                    .effect(h.getEffect())
                    .source(h.getSource() != null ? h.getSource() : h.getSuperSource())
                    .build();
        }
    };

}
