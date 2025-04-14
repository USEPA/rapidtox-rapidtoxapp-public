package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.ccte.api.rapidtox.RapidtoxApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RapidtoxApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ToxcastIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getToxcastModelByDTXSIDTest() throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode fetchToxcastModelJson = om.readTree(mockMvc.perform(get("/toxcast-model/search/by-dtxsid?dtxsid=DTXSID001001267"))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString());
        Assertions.assertTrue(fetchToxcastModelJson.get("_embedded").get("toxcast-model").get(0).get("dtxsid").textValue().equals("DTXSID001001267"));
    }

    @Test
    public void getToxcastModelByDTXSIDTestNoResult() throws Exception {
        ObjectMapper om = new ObjectMapper();
        JsonNode fetchToxcastModelJson = om.readTree(mockMvc.perform(get("/toxcast-model/search/by-dtxsid?dtxsid=kk"))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString());
        System.out.print(fetchToxcastModelJson.get("_embedded").get("toxcast-model") + "\n");
        Assertions.assertTrue(fetchToxcastModelJson.get("_embedded").get("toxcast-model").isEmpty());
    }

    @Test
    public void disabledRepositoryMethodsTest() throws Exception {
        mockMvc.perform(delete("/toxcast-model/402890827f554813017f6b6b8320000b").content("{}"))
                .andExpect(status().is(405));
    }
}
