
package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import java.util.List;
import lombok.Data;

@Data
public class PodDataSelected {

    private long compositeUf;
    private String dtxsid;
    private String id;
    private String interimRfV;
    private long podId;
    private double podValue;
    private String preferredName;
    private String rfvLabel;
    private String section;
    private double toxvalNumeric;
    private String toxvalUnits;
    private double podhed;
    private List<UncertaintyFactorData> uncertaintyFactorData;

}
