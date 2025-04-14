package gov.epa.ccte.api.rapidtox.scatterplot.service;

import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScatterPlotService {

    private final ScatterPlotFetcher fetcher;
    private final HazardRepository hazardRepo;
    
    
}
