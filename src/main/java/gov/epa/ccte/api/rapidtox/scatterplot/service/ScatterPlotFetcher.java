package gov.epa.ccte.api.rapidtox.scatterplot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class ScatterPlotFetcher {

    private String rtplotEndpoint;

    public ScatterPlotFetcher(@Value("${rtplot-api.url}") String rtPlotEndpoint) {
        this.rtplotEndpoint = rtPlotEndpoint;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HazardPlotItem {

        String exposureRoute;
        String studyType;
        String superCategory;
        String toxvalType;
        String toxvalSubtype;
        Double toxvalNumeric;
        String toxvalUnits;
    }

    private static final Set<String> ACCEPTABLE_EXPOSURE_ROUTES = Set.of("oral", "inhalation");

    /**
     *
     * @param dataItems
     * @param exposureRoute
     * @return a BufferedImage of the plot if it was able to be plotted, null
     * otherwise
     * @throws IOException
     */
    public BufferedImage fetchScatterPlotImageFor(List<HazardPlotItem> dataItems, String exposureRoute) throws IOException {
        return fetchScatterPlotImageFor(dataItems, exposureRoute, null);
    }

    /**
     *
     * @param dataItems
     * @param exposureRoute
     * @param predictedThreshold optional, can be null
     * @return a BufferedImage of the plot if it was able to be plotted, null
     * otherwise
     * @throws IOException
     */
    public BufferedImage fetchScatterPlotImageFor(List<HazardPlotItem> dataItems, String exposureRoute, Double predictedThreshold) throws IOException {
        try {
            ScatterPlotResponse response = fetchScatterPlotFor(dataItems, exposureRoute, predictedThreshold);
            BufferedImage image = null;
            try (InputStream is = new ByteArrayInputStream(response.imageData)) {
                image = ImageIO.read(is);
            }

            return image;
        } catch (IOException e) {
            return null;
        }

    }

    public ScatterPlotResponse fetchScatterPlotFor(List<HazardPlotItem> dataItems, String exposureRoute, Double predictedExposure) throws IOException {
        validateDataItems(dataItems);
        validateExposureRoute(exposureRoute);

        log.debug("data-items: {}", dataItems);

        ObjectMapper om = new ObjectMapper();
        String requestBody = om.writeValueAsString(dataItems);

        Map<String, Object> params = new HashMap<>();
        params.put("exposure_route", exposureRoute.toLowerCase());

        if (predictedExposure != null) {
            params.put("predicted_exposure", predictedExposure);
        }

        ScatterPlotResponse response = sendPostRequest(rtplotEndpoint, params, requestBody);
        return response;
    }

    private void validateDataItems(List<HazardPlotItem> dataItems) {
        Assert.notNull(dataItems, "dataItems cannot be null");
//        Assert.notEmpty(dataItems, "dataItems cannot be empty");
    }

    public void validateExposureRoute(String exposureRoute) {
        Assert.notNull(exposureRoute, "exposureRoute cannot be null");
        Assert.isTrue(ACCEPTABLE_EXPOSURE_ROUTES.contains(exposureRoute.toLowerCase()), "exposureRoute must be one of [" + ACCEPTABLE_EXPOSURE_ROUTES.stream().collect(Collectors.joining(",")) + "]");
    }

    private static String expandUrl(String url, Map<String, ?> queryParams) {
        boolean first = true;
        String newUrl = url;
        for (Entry<String, ?> entry : queryParams.entrySet()) {
            if (first) {
                newUrl += "?";
                first = false;
            } else {
                newUrl += "&";
            }
            newUrl += entry.getKey() + "=" + entry.getValue().toString();
        }
        return newUrl;
    }

    public static record ScatterPlotResponse(
            byte[] imageData,
            String descriptiveText) {

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Arrays.hashCode(this.imageData);
            hash = 37 * hash + Objects.hashCode(this.descriptiveText);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ScatterPlotResponse other = (ScatterPlotResponse) obj;
            if (!Objects.equals(this.descriptiveText, other.descriptiveText)) {
                return false;
            }
            return Arrays.equals(this.imageData, other.imageData);
        }

        @Override
        public String toString() {
            return "ScatterPlotResponse{" + "imageData=" + (imageData != null ? "byte[" + imageData.length + "]" : "null") + ", descriptiveText=" + descriptiveText + '}';

        }

    }

    private static ScatterPlotResponse sendPostRequest(String url, Map<String, ?> queryParams, String postData) throws IOException {

        URL obj = new URL(expandUrl(url, queryParams));
        log.debug("attempting to connect to {}", url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.addRequestProperty("content-type", "application/json");
        conn.addRequestProperty("accept", "image/png");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes());
        }

        try (InputStream is = conn.getInputStream()) {
            return new ScatterPlotResponse(IOUtils.toByteArray(is), conn.getHeaderField("descriptive_text"));
        }
    }

}
