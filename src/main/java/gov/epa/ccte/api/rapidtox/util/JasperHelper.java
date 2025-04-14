package gov.epa.ccte.api.rapidtox.util;

import gov.epa.ccte.api.rapidtox.sessionreport.ReportRequest;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.PodJustification;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.PodDataSelected;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.CustomHazard;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.UncertaintyFactorData;
import gov.epa.ccte.api.rapidtox.sessionreport.controller.BerData;
import static gov.epa.ccte.api.rapidtox.config.RapidToxConstants.TREAT_AS_CUSTOM_TOX;
import static gov.epa.ccte.api.rapidtox.config.RapidToxConstants.TREAT_AS_CUSTOM_POD;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class JasperHelper {
    
    private JasperHelper() {}

	public static List<Integer> getHazardSelections(String dtxsid, ReportRequest[] reportRequests) {
		log.trace("");
		ArrayList<Integer> listOfHazardIds = new ArrayList<>();

		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				listOfHazardIds = new ArrayList<>(obj.getData().getHazard());
			}
		}

		if (listOfHazardIds.isEmpty()) {
			listOfHazardIds.add(-1);
		}

		return listOfHazardIds;
	}

	public static Boolean getHasData(String reportType, ReportRequest reportRequests) {

		log.trace("");
		if (reportType.equals("Tox")) {
			return reportRequests.getHasHazardToxData();
		} else if (reportType.equals("Pod")) {
			return reportRequests.getHasHazardPodData();
		} else if (reportType.equals("Physchem")) {
			return reportRequests.getHasPhyschemData();
		} else if (reportType.equals("Env")) {
			return reportRequests.getHasEnvData();
		} else if (reportType.equals("Toxcast")) {
			return reportRequests.getHasToxcastData();
		} else if (reportType.equals("BER")) {
			return reportRequests.getHasBerData();
		} else {
			return false;
		}
	}

	public static Boolean getHasCustomData(ReportRequest reportRequest) {
		log.trace("");
		return !reportRequest.getData().getCustomData().isEmpty();
	}

	public static Boolean getHasData(String reportType, String dtxsid, ReportRequest[] reportRequests) {

		log.trace("");
		Boolean hasData = false;

		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				if (reportType.equals("Tox")) {
					hasData = obj.getHasHazardToxData();
				} else if (reportType.equals("Pod")) {
					hasData = obj.getHasHazardPodData();
				} else if (reportType.equals("Physchem")) {
					hasData = obj.getHasPhyschemData();
				}
			}
		}

		return hasData;
	}

	public static List<Integer> getPhyschemSelections(String dtxsid, ReportRequest[] reportRequests) {
		log.trace("");
		ArrayList<Integer> listOfPhyschemIds = new ArrayList<>();

		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				listOfPhyschemIds = new ArrayList<>(Arrays.asList(obj.getData().getPhyschem()));
			}
		}

		if (listOfPhyschemIds.isEmpty()) {
			listOfPhyschemIds.add(-1);
		}

		return listOfPhyschemIds;
	}

	public static boolean isImage(String url) {
		log.trace("");
		try {
			BufferedImage image = ImageIO.read(new URL(url));

			return image != null;

		} catch (MalformedURLException e) {
			log.error("URL error with image");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			log.error("IO error with image");
			e.printStackTrace();
			return false;
		}
	}

	public static List<String> getAnalogueDtxsidList(ReportRequest[] reportRequests, String dtxsid) {

		log.trace("");
		List<String> dtxsidsList = null;
		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				dtxsidsList = obj.getData().getReportAnalogueDetails().getDtxsidList();
			}

		}
		return dtxsidsList;
	}

	public static List<Integer> getAnaloguePodIdList(ReportRequest[] reportRequests, String dtxsid) {

		log.trace("");
		List<Integer> podIdsList = new ArrayList<>();
		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				podIdsList = obj.getData().getReportAnalogueDetails().getPodIdsList();
			}
		}

		if (podIdsList.isEmpty()) {
			podIdsList.add(-1);
		}

		return podIdsList;
	}

	public static List<Double> getSimilarityList(ReportRequest[] reportRequests, String dtxsid) {

		log.trace("");
		List<Double> similarityList = null;
		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				similarityList = obj.getData().getReportAnalogueDetails().getSimilarityList();
			}
		}

		return similarityList;
	}

	public static ReportRequest getReportRequest(ReportRequest[] reportRequests, String dtxsid) {

		log.trace("");
		ReportRequest reportRequest = null;
		for (ReportRequest obj : reportRequests) {
			if (obj.getDtxsid().equals(dtxsid)) {
				reportRequest = obj;
			}
		}

		return reportRequest;
	}

	public static List<PodDataSelected> getUncertainityList(ReportRequest reportRequest, String dtxsid) {
		log.trace("");
		return reportRequest.getData().getPodDataSelected().stream().filter(c -> c.getDtxsid().equalsIgnoreCase(dtxsid)).collect(Collectors.toList());

	}

	public static List<UncertaintyFactorData> getUncertainityFactorData(ReportRequest reportRequest, int index, String dtxsid) {

		log.trace("");
		List<PodDataSelected> podDataSelecteds = reportRequest.getData().getPodDataSelected().stream().filter(c -> c.getDtxsid().equalsIgnoreCase(dtxsid)).collect(Collectors.toList());

		return podDataSelecteds.get(index).getUncertaintyFactorData();

	}

	public static List<BerData> getBerList(ReportRequest reportRequest) {

		log.trace("");
		return new ArrayList<>(reportRequest.getData().getBer());

	}

	public static List<CustomHazard> getCustomData(ReportRequest reportRequest) {
		log.trace("");
		return new ArrayList<>(reportRequest.getData().getCustomData());
	}

	public static List<CustomHazard> getCustomPODs(ReportRequest reportRequest) {
		log.trace("");
		return reportRequest.getData().getCustomData().stream().filter(data -> data.getDtxsid().equalsIgnoreCase(reportRequest.getDtxsid()) && data.getSuperCategory().equalsIgnoreCase(TREAT_AS_CUSTOM_POD)).collect(Collectors.toList());

	}

	public static List<CustomHazard> getCustomToxValues(ReportRequest reportRequest) {
		log.trace("");
		return reportRequest.getData().getCustomData().stream().filter(data -> data.getDtxsid().equalsIgnoreCase(reportRequest.getDtxsid()) && data.getSuperCategory().equalsIgnoreCase(TREAT_AS_CUSTOM_TOX)).collect(Collectors.toList());
	}

	public static String getJustification(List<PodJustification> podJustifications, Integer podId) {

		log.trace("");
		PodJustification podJustification = podJustifications.stream().filter(justification -> Objects.equals(podId, justification.getPodId())).findFirst().orElse(null);

		if (podJustification != null) {
			return podJustification.getJustification();
		} else {
			return "";
		}

	}

	public static String toPrecision(double d, int digits) {
		String s = String.format("%." + ((digits > 0) ? digits : 16) + "g", d).replace("e+0", "e+").replace("e-0", "e-");
		return s;
	}

	public static String formatNullableString(String string) { 
		if (string == null) { 
			return "-";
		}
		return string;
	}
	
	public static String formatDoubleValue(Double value) {
		if (value == null) { 
			return "-";
		}
		if ((value >= 0.1 && value < 1000) || (value <= -0.1 && value > -1000) || value == 0) {
			return toPrecision(value, 3);
		} else {
			return String.format("%6.2e", value);
		}
	}
	
	public static boolean isNumeric(Number value) { 
		if (value != null) { 
			return true;
		}
		return false;
	}

	public static boolean isNumeric(String str) {
		try {
			Double.valueOf(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String formatDoubleAsStringValue(String value) {
		return formatDoubleValue(Double.valueOf(value));
	}

	public static String formatDoubleRangeValue(Double min, Double max) {

		if (min == null && max == null) {
			return "-";
		}
		if (min == null) {
			return formatDoubleValue(max);
		}
		if (max == null) {
			return formatDoubleValue(min);
		}
		if (min.equals(max)) {
			return formatDoubleValue(max);
		} else {
			return formatDoubleValue(min) + " to " + formatDoubleValue(max);
		}
	}

}
