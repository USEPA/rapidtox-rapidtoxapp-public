package gov.epa.ccte.api.rapidtox.scatterplot.service;

import gov.epa.ccte.api.rapidtox.sessionreport.HazardDTO;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardDtoPlotItemMapper;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardHydrationService;
import gov.epa.ccte.api.rapidtox.hazard.service.HazardPlotItemMapper;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.HazardPlotItem;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.ScatterPlotResponse;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/chart/proxy")
public class RapidToxPlotProxy {

    private final ScatterPlotFetcher fetcher;
    private final HazardHydrationService hazardSvc;
    private final HazardDtoPlotItemMapper mapper;

    @PostMapping(value = "/single-chemical", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<?> generateImage(@RequestBody ReportRequest req) {

        try {
            
            List<HazardDTO> selectedHazards = hazardSvc.getSelectedHazardDtos(req);
            List<HazardPlotItem> dataItems = selectedHazards.stream().map(mapper::map).collect(Collectors.toList());
            Double predictedExposure = req.getData().getPredictedExposure();

            ScatterPlotResponse response = fetcher.fetchScatterPlotFor(dataItems, req.getChartType(), predictedExposure);
            return buildResponse(response.imageData(), response.descriptiveText());
        } catch (IllegalArgumentException | IllegalStateException e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return ResponseEntity.badRequest().headers(headers).body(e.getMessage());
        } catch (IOException e) {
            log.warn("error fetching image {} (probably nothing to render); returning EmptyPlot.png", e.getMessage());
            try {
                File classPathInput = ResourceUtils.getFile(this.getClass().getResource("/images/").toURI() + "EmptyPlot.png");
                byte[] imageBytes = Files.readAllBytes(classPathInput.toPath());
                return buildResponse(imageBytes, "Image saying 'No plottable points found in selected data.' "
                        + "Then, more text saying 'Select oral items with mg/kg-day or inhalation items with mg/m3.'");
            } catch (IOException | URISyntaxException ex) {
                log.error("unable to send empty-plot image: {}", ex.getMessage());
            }
        }
        return null;
    }

    ResponseEntity<byte[]> buildResponse(byte[] imageBytes, String descriptiveText) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("descriptive_text", descriptiveText);
        headers.setAccessControlExposeHeaders(List.of("descriptive_text"));
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity(imageBytes, headers, HttpStatus.OK);
    }
}
