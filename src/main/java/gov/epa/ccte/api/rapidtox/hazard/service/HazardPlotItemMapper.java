package gov.epa.ccte.api.rapidtox.hazard.service;

import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.HazardPlotItem;
import gov.epa.ccte.api.rapidtox.util.Mapper;
import org.springframework.stereotype.Component;

@Component
public class HazardPlotItemMapper implements Mapper<Hazard, HazardPlotItem> {

    @Override
    public HazardPlotItem map(Hazard h) {
        // TODO: fix upstream data? -- setting exposure route to lower case to overcome inconsistencies in data
        return HazardPlotItem.builder()
                .exposureRoute(h.getExposureRoute().toLowerCase())
                .studyType(h.getStudyType())
                .superCategory(h.getSuperCategory())
                .toxvalNumeric(h.getToxvalNumeric())
                .toxvalUnits(h.getToxvalUnits())
                .build();
    }

}
