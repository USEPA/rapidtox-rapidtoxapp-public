package gov.epa.ccte.api.rapidtox.scatterplot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotGenerator;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import org.springframework.http.MediaType;

import static org.springframework.http.MediaType.IMAGE_PNG;

@Slf4j
@RestController
@RequestMapping(value = "chart")
public class ScatterChart {

    @PostMapping(value = "/single-chemical")
    public ResponseEntity<byte[]> generateImage(@RequestBody String reportRequestStr) {
        log.info("json request: {}", reportRequestStr);
        ObjectMapper om = new ObjectMapper();

        try {
            ReportRequest reportRequest = om.readValue(reportRequestStr, ReportRequest.class);
            Image image = ScatterPlotGenerator.buildScatterChart(reportRequest, reportRequest.getSection(), reportRequest.getChartType(), reportRequest.getWidth(), reportRequest.getHeight());

            if (image != null) {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write((BufferedImage) image, "png", outputStream);

                byte[] bytes = outputStream.toByteArray();

                return ResponseEntity.ok().contentType(IMAGE_PNG).body(bytes);
            } else {
                return ResponseEntity.ok().contentType(IMAGE_PNG).body(null);
            }

        } catch (final Exception e) {
            log.error("Some error has occurred while generating image");
            log.error(e.getMessage());
            return null;
        }
    }
}
