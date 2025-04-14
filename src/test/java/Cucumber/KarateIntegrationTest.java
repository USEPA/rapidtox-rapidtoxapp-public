package Cucumber;
import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import gov.epa.ccte.api.rapidtox.RapidtoxApplication;
import org.apache.coyote.Response;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
//

@RunWith(Cucumber.class)
@CucumberOptions(features ="classpath:karate")
@SpringBootTest(classes = RapidtoxApplication.class)
public class KarateIntegrationTest {

}
