package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import org.springframework.stereotype.Component;

@Component
public class CustomHazardMapper implements Mapper<CustomHazard, Hazard> {

    @Override
    public Hazard map(CustomHazard cd) {
        return Hazard.builder()
                .dtxsid(cd.getDtxsid())
                .casrn(cd.getCasrn())
                .effect(cd.getEffect())
                .exposureMethod(cd.getExposureMethod())
                .exposureRoute(cd.getExposureRoute())
                .preferredName(cd.getPreferredName())
                .riskAssessmentClass(cd.getRiskAssessmentClass())
                .source(cd.getSource())
                .speciesCommon(cd.getSpecies())
                .studyDuration(cd.getStudyDuration())
                .studyDurationUnits(cd.getStudyDurationUnits())
                .studyType(cd.getStudyType())
                .superCategory(cd.getSuperCategory())
                .superSource(cd.getSuperSource())
                .toxvalNumeric(cd.getToxvalNumeric())
                .toxvalSubtype(cd.getToxvalSubtype())
                .toxvalTypDefn(cd.getToxvalTypDefn())
                .toxvalType(cd.getToxvalType())
                .toxvalUnits(cd.getToxvalUnits())
                .build();
    }

}
