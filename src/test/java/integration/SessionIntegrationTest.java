package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.ccte.api.rapidtox.RapidtoxApplication;
import gov.epa.ccte.api.rapidtox.session.controller.UpdateSessionRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = RapidtoxApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SessionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createNewSessionRecordSuccess () throws Exception{
                mockMvc.perform(post("/session-recall")
                                .contentType(MediaType.APPLICATION_JSON)
                                                .content("""
                                                         {"username":"ndsilva",
                                                         "sessionTitle":"testSession"}"""))
                        .andExpect(status().isCreated())
                        .andExpect(status().is2xxSuccessful());
    }


    @Test
    public void getSessionBySessionKeyTest() throws Exception{
        ObjectMapper om = new ObjectMapper();
        //create new session
        JsonNode createSessionJson = om.readTree(        mockMvc.perform(post("/session-recall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {"username":"ndsilva",
                                 "sessionTitle":"testSession"}"""))
                .andExpect(status().isCreated())
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString());
        //retrieve session based on created sessionKey
        String sessionKey = createSessionJson.get("sessionKey").asText();
        JsonNode fetchSessionJson = om.readTree(mockMvc.perform(get("/session-recall/search/by-sessionkey?sessionKey="+sessionKey))
                .andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString());
        Assertions.assertTrue(fetchSessionJson.get("sessionTitle").textValue().equals("testSession"));
    }

    @Test
    public void getSessionBySessionKeyNotFoundTest() throws Exception{
        mockMvc.perform(get("/session-recall/search/by-sessionkey?sessionKey=test")).andExpect(status().isNotFound());
    }

    @Test
    public void updateSessionRecordInvalidSessionKey() throws Exception{
        ObjectMapper om = new ObjectMapper();
        UpdateSessionRequest updateRequest = new UpdateSessionRequest("pod", "pod", "{pod_data: some value}", "bad session key");
        mockMvc.perform(post("/session-recall/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void corsUnauthorizedRequestTest () throws Exception{
        mockMvc.perform(options("/session-recall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Access-Control-Request-Method", "PURGE")
                        .header("Origin", "nefarious origin")
                        .content("{}"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().is(403));
    }

    @Test
    public void disabledRepositoryMethodTest() throws Exception{
        mockMvc.perform(delete("/session-recall")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().is4xxClientError());
    }
}

