package integration;

import gov.epa.ccte.api.rapidtox.RapidtoxApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RapidtoxApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class BioactivityResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBioactivityBerSuccess() throws Exception {
        mockMvc.perform(post("/bioactivity-summary/ber/search/by-dtxsids")
                .content("[ \"/DTXSID9045479\", \"DTXSID0020286\"]")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testBioactivityPlotSuccess() throws Exception {
        mockMvc.perform(get("/bioactivity-summary/plot/search/by-dtxsid/DTXSID0020286"))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful());
    }

}
