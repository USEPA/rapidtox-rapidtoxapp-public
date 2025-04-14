package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.sessionreport.HazardDTO;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.HazardPlotItem;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import org.springframework.stereotype.Component;

@Component
public class HazardDtoPlotItemMapper implements Mapper<HazardDTO, HazardPlotItem> {

    @Override
    public HazardPlotItem map(HazardDTO h) {
        // fix upstream data? -- setting exposure route to lower case to overcome inconsistencies in data
        return HazardPlotItem.builder()
                .exposureRoute(h.getExposureRoute().toLowerCase())
                .studyType(h.getStudyType())
                .superCategory(h.getSuperCategory())
                .toxvalType(h.getToxvalType())
                .toxvalSubtype(h.getToxvalSubtype())
                .toxvalNumeric(h.getToxvalNumeric())
                .toxvalUnits(h.getToxvalUnits())
                .build();
    }

}
