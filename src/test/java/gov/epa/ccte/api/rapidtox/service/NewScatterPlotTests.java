package gov.epa.ccte.api.rapidtox.service;

import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher;
import gov.epa.ccte.api.rapidtox.scatterplot.service.ScatterPlotFetcher.HazardPlotItem;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;

public class NewScatterPlotTests {

    @Test
    public void xxx() throws Exception {
        List<HazardPlotItem> dataItems = List.of(
                new HazardPlotItem("inhalation", "developmental", "Dose Response Summary Value", "NOEL", "subbie", 12.34, "mg/m3")
        );

        ScatterPlotFetcher svc = new ScatterPlotFetcher("https://rtplot-dev.ccte.epa.gov/plot");        

        BufferedImage img = svc.fetchScatterPlotImageFor(dataItems, "inhalation");
        assertThat(img).isNotNull();
        ImageIO.write(img, "png", new File("image.png"));
    }

}
