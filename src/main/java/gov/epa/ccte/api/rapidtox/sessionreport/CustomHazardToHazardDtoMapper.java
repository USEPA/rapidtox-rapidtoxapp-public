package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.util.Mapper;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import org.springframework.stereotype.Component;

@Component
public class CustomHazardToHazardDtoMapper implements Mapper<CustomHazard, HazardDTO> {

    @Override
    public HazardDTO map(CustomHazard h) {
        return HazardDTO.builder()
                .id(h.getId())
                .dtxsid(h.getDtxsid())
                .preferredName(h.getPreferredName())
                .casrn(h.getCasrn())
                .superCategory(h.getSuperCategory())
                .studyType(h.getStudyType())
                .studyDuration(h.getStudyDuration())
                .studyDurationUnits(h.getStudyDurationUnits())
                .species(h.getSpecies())
                .toxvalType(h.getToxvalType())
                .toxvalSubtype(h.getToxvalSubtype())
                .toxvalNumeric(h.getToxvalNumeric())
                .toxvalUnits(h.getToxvalUnits())
                .riskAssessmentClass(h.getRiskAssessmentClass())
                .exposureRoute(h.getExposureRoute())
                .exposureMethod(h.getExposureMethod())
                .source(h.getSource() != null ? h.getSource() : h.getSuperSource())
                .effect(h.getEffect())
                .toxvalTypDefn(h.getToxvalTypDefn())
                .build();
    }

}
