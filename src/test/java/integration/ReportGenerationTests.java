package integration;

import gov.epa.ccte.api.rapidtox.RapidtoxApplication;
import gov.epa.ccte.api.rapidtox.service.Constants;
import jakarta.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RapidtoxApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ReportGenerationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenRequest_shouldGenerateReport() throws Exception {
        byte[] responseBytes = mockMvc.perform(post("/report/single-chemical/pdf")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Constants.REQUEST_STRING))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        try (OutputStream os = new FileOutputStream("single-report.pdf")) {
            os.write(responseBytes);
            os.flush();
        }
    }
}
