package gov.epa.ccte.api.rapidtox.sessionreport;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import org.springframework.stereotype.Component;

@Component
public class HazardToHazardDtoMapper implements Mapper<Hazard, HazardDTO> {

    @Override
    public HazardDTO map(Hazard h) {
        return HazardDTO.builder()
                .id(h.getId())
                .dtxsid(h.getDtxsid())
                .preferredName(h.getPreferredName())
                .casrn(h.getCasrn())
                .molecularWeight(h.getMolecularWeight())
                .superCategory(h.getSuperCategory())
                .studyType(h.getStudyType())
                .studyDuration(h.getStudyDuration())
                .studyDurationUnits(h.getStudyDurationUnits())
                .species(h.getSpeciesCommon())
                .toxvalType(h.getToxvalType())
                .toxvalSubtype(h.getToxvalSubtype())
                .toxvalNumeric(h.getToxvalNumeric())
                .toxvalUnits(h.getToxvalUnits())
                .riskAssessmentClass(h.getRiskAssessmentClass())
                .exposureRoute(h.getExposureRoute())
                .exposureMethod(h.getExposureMethod())
                .source(h.getSource())
                .effect(h.getEffect())
                .toxvalTypDefn(h.getToxvalTypDefn())
                .sourceUrl(h.getSourceUrl())
                .ssourceUrl(h.getSsourceUrl())
                .clowderId(h.getClowderId())
                .workflow(h.getWorkflow())
                .build();
    }
}
