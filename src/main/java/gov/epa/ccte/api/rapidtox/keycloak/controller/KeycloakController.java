package gov.epa.ccte.api.rapidtox.keycloak.controller;

import gov.epa.ccte.api.rapidtox.keycloak.model.KeyCloakResponse;
import gov.epa.ccte.api.rapidtox.keycloak.controller.KeycloakUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(value = "keycloak")
@Deprecated
public class KeycloakController {

    @Value("${keycloak.username}")
    private String username;

    @Value("${keycloak.password}")
    private String password;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    @Value("${keycloak.clientId}")
    private String clientId;


    @GetMapping("/all-users")
    public List<KeycloakUser> getListOfUsers() throws IOException {

        List<NameValuePair> urlParameters = new ArrayList<>();

// add any number of form data
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("client_id", clientId));
        urlParameters.add(new BasicNameValuePair("client_secret", clientSecret));

// Getting the HTTP Response and processing it
        String httpUrl = "https://ccte-keycloak-dev.epa.gov/auth/realms/rapidtox/protocol/openid-connect/token";
        HttpResponse response = postWithFormData(httpUrl, urlParameters);

        String responseBody = EntityUtils.toString(response.getEntity());

        JSONObject json = new JSONObject(responseBody);
        String token = json.get("access_token").toString();

        return getListOfUsers(token);

    }

    private HttpResponse postWithFormData(String url, List<NameValuePair> params) throws IOException {
        RequestConfig requestConfig = RequestConfig.custom().build();

        // building http client
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(url);

        // adding the form data
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return httpClient.execute(httpPost);
    }

    private List<KeycloakUser> getListOfUsers(String token) {

        //get call to keycloak url with rest template
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://ccte-keycloak-dev.epa.gov/auth/admin/realms/rapidtox/users";

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<String>(headers);

            ResponseEntity<KeyCloakResponse[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, KeyCloakResponse[].class);

            List<KeycloakUser> resultList = new ArrayList<>();
            for (KeyCloakResponse kc : Objects.requireNonNull(response.getBody())
            ) {
                // Filtering out rapidtox user accounts
                if (!kc.getUsername().contains("rapidtox")) {
                    KeycloakUser keycloakUser = new KeycloakUser(kc.getUsername());
                    resultList.add(keycloakUser);
                }
            }

            return resultList;
        } catch (Exception ex) {
            log.error("Error message: " + ex.getMessage());
            return null;
        }
    }
}
