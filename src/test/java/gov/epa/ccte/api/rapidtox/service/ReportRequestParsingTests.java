package gov.epa.ccte.api.rapidtox.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

public class ReportRequestParsingTests {

    @Test
    void givenRequest_shouldBeAbleToParseRequest() throws Exception {
        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS);

        ReportRequest rr = om.readValue(Constants.REQUEST_STRING, ReportRequest.class);
        assertThat(rr).isNotNull();
        System.out.println(rr);
    }
}
