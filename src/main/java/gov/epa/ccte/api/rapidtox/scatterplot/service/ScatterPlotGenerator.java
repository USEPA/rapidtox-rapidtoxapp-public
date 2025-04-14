package gov.epa.ccte.api.rapidtox.scatterplot.service;

import static gov.epa.ccte.api.rapidtox.config.RapidToxConstants.TREAT_AS_POD;
import gov.epa.ccte.api.rapidtox.hazard.model.Hazard;
import gov.epa.ccte.api.rapidtox.hazard.repository.HazardRepository;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RectangleInsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class ScatterPlotGenerator {

	private static final String ORAL_UNITS = "mg/kg-day";
	private static final String INHALATION_UNITS = "mg/m3";
	private static final String CHART_SUPER_CATEGORY = TREAT_AS_POD;

	// Private constructor to prevent instantiation
	private static HazardRepository hazardRepository;

	@Autowired
	private ScatterPlotGenerator(HazardRepository hazardRepository) {
		ScatterPlotGenerator.hazardRepository = hazardRepository;
	}

	private ScatterPlotGenerator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Build a PNG image of a pie chart example about the "Top IDE index".
	 *
     * @param reportRequest data container
     * @param section section of the report we're generating for
     * @param chartType oral/inhalation
	 * @param width Width of the image.
	 * @param height Height of the image.
     * @return generated scatter plot image
	 */
	public static Image buildScatterChart(ReportRequest reportRequest, String section, String chartType, int width, int height) {// needs to be just
		//check for section (hazard,analogue,bioActivity)
		try {
			String units = "oral".equalsIgnoreCase(chartType) ? ORAL_UNITS : INHALATION_UNITS;

			String title = section + " " + chartType + " Scatter Chart";

			List<Hazard> hazardList = new ArrayList<>();
			List<CustomHazard> details = new ArrayList<>();
			if (!reportRequest.getData().getCustomData().isEmpty()) {
				details = reportRequest.getData().getCustomData().stream()
						.filter(detail -> detail.getDtxsid().equals(reportRequest.getDtxsid())).collect(Collectors.toList()).stream()
						.filter(detail -> detail.getExposureRoute().equalsIgnoreCase(chartType)).collect(Collectors.toList());
			}
			if (section.equalsIgnoreCase("In Vivo Toxicity")) {
				if (chartType.equalsIgnoreCase("oral")) {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getHazard(), CHART_SUPER_CATEGORY, units);
				} else {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getHazard(), CHART_SUPER_CATEGORY, units);
				}
			} else if (section.equalsIgnoreCase("analogue")) {
				if (chartType.equalsIgnoreCase("oral")) {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getReportAnalogueDetails().getPodIdsList(), CHART_SUPER_CATEGORY, units);
				} else {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getReportAnalogueDetails().getPodIdsList(), CHART_SUPER_CATEGORY, units);
				}

			} else if (section.equalsIgnoreCase("bioactivity")) {
				//
			} else {
				//y values
				//it could be per chemical not per section
			}
			if (hazardList.size() == 1) {
				hazardList.remove(0);
			}
			List<String> uniqueList = new ArrayList<>();
			Set<String> dataSourceSet = new HashSet<>();
			for (int i = 0; i < hazardList.size(); i++) {
				dataSourceSet.add(hazardList.get(i).getSuperSource());
			}
			if (!details.isEmpty()) {
				for (int i = 0; i < details.size(); i++) {
					dataSourceSet.add(details.get(i).getSource());
				}
			}
			uniqueList.addAll(dataSourceSet);
			DefaultXYDataset dataset = generateSeries(units, hazardList, details, uniqueList);

			JFreeChart chart = createChart(dataset, title, uniqueList, units);

			if (chart != null) {
				// Changes background color
				XYPlot plot = (XYPlot) chart.getPlot();
				plot.setBackgroundPaint(new Color(105, 105, 105));
				XYShapeRenderer renderer0 = new XYShapeRenderer();
				plot.setRenderer(0, renderer0);
				plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.blue);
			}

			return imageToPng(chart, width, height);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}
	}

	public static boolean isDataAvailable(ReportRequest reportRequest, String chartType, String section) {
		try {
			List<Hazard> hazardList = new ArrayList<>();
			List<CustomHazard> details = new ArrayList<>();
			if (section.equalsIgnoreCase("In Vivo Toxicity")) {
				if (chartType.equalsIgnoreCase("oral")) {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getHazard(), CHART_SUPER_CATEGORY, ORAL_UNITS);
					details = reportRequest.getData().getCustomData().stream().filter(item -> item.getExposureRoute().equalsIgnoreCase("oral")).collect(Collectors.toList());
				} else {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getHazard(), CHART_SUPER_CATEGORY, INHALATION_UNITS);
					details = reportRequest.getData().getCustomData().stream().filter(item -> item.getExposureRoute().equalsIgnoreCase("inhalation")).collect(Collectors.toList());
				}
			}
			if (section.equalsIgnoreCase("Analogue")) {
				if (chartType.equalsIgnoreCase("oral")) {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getReportAnalogueDetails().getPodIdsList(), CHART_SUPER_CATEGORY, ORAL_UNITS);
//                    details = reportRequest.getData().getCustomData().stream().filter(item -> item.getExposureRoute().equalsIgnoreCase("oral")).collect(Collectors.toList());
				} else {
					hazardList = hazardRepository.findByIdInAndSuperCategoryEqualsAndToxvalUnitsEquals(reportRequest.getData().getReportAnalogueDetails().getPodIdsList(), CHART_SUPER_CATEGORY, INHALATION_UNITS);
//                    details = reportRequest.getData().getCustomData().stream().filter(item -> item.getExposureRoute().equalsIgnoreCase("inhalation")).collect(Collectors.toList());

				}
			}

			return hazardList.size() > 1 || details.size() > 1;

		} catch (Exception ex) {
			log.error(ex.getMessage());
			return false;
		}
	}

	private static DefaultXYDataset generateSeries(String title, List<Hazard> hazardList, List<CustomHazard> customData,
			List<String> uniqueList) {
		DefaultXYDataset result = new DefaultXYDataset();
		XYSeries repositoryDataSeries = new XYSeries(title);
		XYSeries customDataSeries = new XYSeries(title);
		for (Hazard hazObj : hazardList) {
			repositoryDataSeries.add((Number)hazObj.getToxvalNumeric(), uniqueList.indexOf(hazObj.getSuperSource()));
		}

		result.addSeries("Repository Data", repositoryDataSeries.toArray());
		if (!customData.isEmpty()) {
			for (CustomHazard detail : customData) {
				customDataSeries.add((Number) detail.getToxvalNumeric(), uniqueList.indexOf(detail.getSuperSource()));
			}
			result.addSeries("User Supplied Data", customDataSeries.toArray());
		}
		return result;
	}

	private static JFreeChart createChart(XYDataset dataset, String title, List<String> uniqueList, String units) {

		try {
			JFreeChart chart = ChartFactory.createScatterPlot(title, // chart title
					"Value",
					"Activation",
					dataset, // data
					PlotOrientation.HORIZONTAL,
					true, // include legend
					true,
					false);

			// create the dataset...
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setForegroundAlpha(0.5f);

			LogAxis xAxis = new LogAxis("Log Value in " + units);
			xAxis.setBase(10);
			DecimalFormat format = new DecimalFormat("0.0");
			xAxis.setNumberFormatOverride(format);
			xAxis.setMinorTickMarksVisible(true);
			xAxis.setLowerMargin(0.05);
			xAxis.setUpperMargin(0.05);

			plot.setDomainAxis(xAxis);

			String[] array = uniqueList.toArray(String[]::new);
			SymbolAxis rangeAxis = new SymbolAxis("Source", array);
			if (uniqueList.isEmpty()) {
				return null;
			} else {
				rangeAxis.setRange(-1, uniqueList.size());
			}
			rangeAxis.setUpperMargin(0.70);
			rangeAxis.setLowerMargin(0.70);
			rangeAxis.setVerticalTickLabels(true);
			plot.setRangeAxis(rangeAxis);

			chart.setPadding(new RectangleInsets(10, 10, 10, 10));

			return chart;
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}

	}

	private static Image imageToPng(final JFreeChart chart, final int width, final int height) {

		try {
			if (chart != null) {
				final BufferedImage bufferedImage = chart.createBufferedImage(width, height);

				ImageIcon imageIcon = new ImageIcon(bufferedImage);

				return imageIcon.getImage();
			} else {
				return null;
			}
		} catch (Exception ex) {
			log.error(ex.getMessage());
			return null;
		}

	}
}
