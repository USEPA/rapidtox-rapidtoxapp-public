package gov.epa.ccte.api.rapidtox.physchem.service;



import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemExperimental;
import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemPredicted;
import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemSummary;
import gov.epa.ccte.api.rapidtox.physchem.model.PhyschemSummaryDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Slf4j
public final class PhyschemUtil {

    // Private constructor to prevent instantiation
    private PhyschemUtil() {
        throw new UnsupportedOperationException();
    }

    public static List<PhyschemSummaryDetails> build(List<PhyschemSummary> summaries,
                                                     List<PhyschemExperimental> experimentals,
                                                     List<PhyschemPredicted> predicteds){

        log.debug("building PhyschemSummaryDetails list");

        Hashtable<String, List<PhyschemExperimental>> expLookup = getExpLookup(experimentals);
        Hashtable<String,List< PhyschemPredicted>> predLookup = getPredLookup(predicteds);

        List<PhyschemSummaryDetails> physchemSummaryDetails = new ArrayList<>();
        List<PhyschemPredicted> predictedList;
        List<PhyschemExperimental> experimentalList;

        for(PhyschemSummary summary : summaries){
            PhyschemSummaryDetails  details = convert(summary);

            String key = summary.getDtxsid() + "-" + summary.getProperty();


            if(expLookup.containsKey(key))
                experimentalList = expLookup.get(key);
            else
                experimentalList = new ArrayList<>();

            if(predLookup.containsKey(key))
                predictedList = predLookup.get(key);
            else
                predictedList = new ArrayList<>();

            details.setPredictedData(predictedList);
            details.setExperimentalData(experimentalList);

            physchemSummaryDetails.add(details);
        }

        log.debug("physchemSummaryDetails count = {}", physchemSummaryDetails.size());

        return physchemSummaryDetails;
    }

    private static Hashtable<String, List<PhyschemPredicted>> getPredLookup(List<PhyschemPredicted> predicteds) {

        Hashtable<String, List<PhyschemPredicted>>  preds = new Hashtable<>();

        String oldKey = "";
        List<PhyschemPredicted> duplicates = new ArrayList<>();

        for(PhyschemPredicted pred : predicteds){
            String key = pred.getDtxsid() + "-" + pred.getProperty();
            if(oldKey.equals("")){
                // first record
                duplicates.add(pred);
                oldKey = key;
            }else if (oldKey.equalsIgnoreCase(key)){
                duplicates.add(pred);
            }else{
                preds.put(oldKey, duplicates);
                oldKey = key;
                duplicates = new ArrayList<>();
                duplicates.add(pred);
            }
        }
        // last record
        preds.put(oldKey, duplicates);

        return preds;
    }

    private static Hashtable<String, List<PhyschemExperimental>> getExpLookup(List<PhyschemExperimental> experimentals) {

        Hashtable<String, List<PhyschemExperimental>> exps = new Hashtable<>();

        String oldKey = "";
        List<PhyschemExperimental> duplicates = new ArrayList<>();

        for(PhyschemExperimental exp : experimentals){
            String key = exp.getDtxsid() + "-" + exp.getProperty();
            if(oldKey.equals("")){
                // first record
                duplicates.add(exp);
                oldKey = key;
            }else if (oldKey.equalsIgnoreCase(key)){
                duplicates.add(exp);
            }else{
                exps.put(oldKey, duplicates);
                oldKey = key;
                duplicates = new ArrayList<>();
                duplicates.add(exp);
            }
        }
        // last record
        exps.put(oldKey, duplicates);

        return exps;
    }

    private static List<PhyschemPredicted> getPredictedData(String dtxsid, String property, List<PhyschemPredicted> predicteds) {

        List<PhyschemPredicted> predictedList = new ArrayList<>();

        for(PhyschemPredicted predicted : predicteds){
            if(predicted.getDtxsid().equalsIgnoreCase(dtxsid) && predicted.getProperty().equalsIgnoreCase(property)){
                predictedList.add(predicted);
            }
        }

        return predictedList;
    }

    private static List<PhyschemExperimental> getExperimentalData(String dtxsid, String property, List<PhyschemExperimental> experimentals) {

        List<PhyschemExperimental> exps  = new ArrayList<>();

        for(PhyschemExperimental exp : experimentals){
            if(exp.getDtxsid().equalsIgnoreCase(dtxsid) && exp.getProperty().equalsIgnoreCase(property)){
                exps.add(exp);
            }
        }

        return exps;
    }

    private static PhyschemSummaryDetails convert(PhyschemSummary summary) {
        PhyschemSummaryDetails details = new PhyschemSummaryDetails();

        details.setId(summary.getId());
        details.setDtxsid(summary.getDtxsid());
        details.setPreferredName(summary.getPreferredName());
        details.setProperty(summary.getProperty());
        details.setUnit(summary.getUnit());
        details.setEnvFateInd(summary.getEnvFateInd());
        details.setExpMin(summary.getExpMin());
        details.setExpMax(summary.getExpMax());
        details.setExpMean(summary.getExpMean());
        details.setExpMedian(summary.getExpMedian());
        details.setPredMin(summary.getPredMin());
        details.setPredMax(summary.getPredMax());
        details.setPredMean(summary.getPredMean());
        details.setPredMedian(summary.getPredMedian());

        return details;
    }
}
