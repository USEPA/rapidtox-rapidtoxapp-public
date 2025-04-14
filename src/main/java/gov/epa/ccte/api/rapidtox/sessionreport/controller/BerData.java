package gov.epa.ccte.api.rapidtox.sessionreport.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BerData {

    private String dtxsid;

    private String podDropDown;

    private String exposureValue;

    private String exposureUnits;

    private String ber;

    private String type;

    @JsonProperty(value = "default")
    private Boolean defaultValue;

}
