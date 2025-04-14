package gov.epa.ccte.api.rapidtox.sessionreport.service;

import gov.epa.ccte.api.rapidtox.sessionreport.model.SessionReport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.apache.http.entity.ContentType.DEFAULT_BINARY;

import java.io.IOException;
import java.util.Random;
import gov.epa.ccte.api.rapidtox.sessionreport.repository.SessionReportRepository;

@Slf4j
@Component
@Transactional
public class SessionReportService {

    private final SessionReportRepository generatedReportRepository;

    @Value("${clowder.base-url}")
    private String clowderBaseUrl;

    @Value("${clowder.write-key}")
    private String apiKey;

    @Value("${clowder.read-key}")
    private String readKey;

    @Value("${clowder.single}")
    private String datasetSingle;

    @Value("${clowder.batch}")
    private String datasetBatch;

    public SessionReportService(SessionReportRepository generatedReportRepository) {
        this.generatedReportRepository = generatedReportRepository;
    }

    public String postReportToClowder(String dtxsid, String reportType, byte[] bytes, String reportDesc, String sessionKey, String username) throws IOException {

        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String dataset;
        if (reportType.equals("SINGLE")) {
            dataset = datasetSingle;
        } else {
            dataset = datasetBatch;
        }
        try {
            HttpPost httppost = new HttpPost(String.format("%s/uploadToDataset/%s?key=%s", clowderBaseUrl, dataset, apiKey));

            LocalDate dateObj = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = dateObj.format(formatter);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (reportType.equals("SINGLE")) {
                builder.addBinaryBody("File", bytes, CONTENT_TYPE_APPLICATION_PDF,
                        dtxsid + "_" + reportType + "_" + sessionKey + "_" + date
                        + (reportType.equals("SINGLE") ? ".pdf" : ".zip"));
            } else {
                builder.addBinaryBody("File", bytes, CONTENT_TYPE_APPLICATION_ZIP,
                        reportType + "_" + sessionKey + "_" + date
                        + (reportType.equals("SINGLE") ? ".pdf" : ".zip"));
            }
            HttpEntity entity = builder.build();
            httppost.setEntity(entity);
            response = httpclient.execute(httppost);
        } catch (Exception ex) {
            log.error("Exception during the http post request in RapidtoxService. Error message: " + ex.getMessage());
        }

        String clowderId = null;
        SessionReport generatedReport = new SessionReport();

        if (response.getEntity() != null) {

            try {

                String responseBody = EntityUtils.toString(response.getEntity());

                JSONObject json = new JSONObject(responseBody);
                clowderId = json.get("id").toString();
                generatedReport.setSessionKey(sessionKey);
                generatedReport.setReportDesc(reportDesc);
                generatedReport.setReportType(reportType);
                generatedReport.setClowderId(clowderId);
                generatedReport.setCreatedBy(username);
                generatedReportRepository.save(generatedReport);

            } catch (Exception ex) {
                log.error("Exception during the parsing of the response in RapidtoxService. Error message: " + ex.getMessage());
                response.close();
                httpclient.close();
            }
        }

        response.close();
        httpclient.close();

        return String.format("%s/files/%s/blob?key=%s", clowderBaseUrl, clowderId, readKey);
    }
    private static final ContentType CONTENT_TYPE_APPLICATION_ZIP = ContentType.create("application/zip");
    private static final ContentType CONTENT_TYPE_APPLICATION_PDF = ContentType.create("application/pdf");
}
