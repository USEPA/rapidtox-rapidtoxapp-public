package integration;

import gov.epa.ccte.api.rapidtox.RapidtoxApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RapidtoxApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class LandscapeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLandscapeReportsSuccess() throws Exception {
        mockMvc.perform(post("/landscape/landscape-reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {"dtxsids":["DTXSID9020111","DTXSID9020110","DTXSID9020118"],
                         "workflow":"er",
                         "sessionKey":"147548"}"""))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testLandscapeReportsClowderIdSuccess() throws Exception {
        mockMvc.perform(get("/landscape/landscape-reports/clowder-id?dtxsid=DTXSID5020576&sessionKey=2c92808691094aa801910a246aa90028&workflow=hha"))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testLandscapeReportsBadRequest() throws Exception {
        mockMvc.perform(post("/landscape/landscape-reports")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {"dtxsids":["DTXSID9020111","DTXSID9020110","DTXSID9020118"],
                         "sessionKey""147548"}"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLandscapeReportsNotFound() throws Exception {
        mockMvc.perform(post("/landscape/landscape-repor")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {"dtxsids":["DTXSID9020111","DTXSID9020110","DTXSID9020118"],
                         "sessionKey""147548"}"""))
                .andExpect(status().isNotFound());
    }
}
