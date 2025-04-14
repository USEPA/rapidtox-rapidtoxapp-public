package gov.epa.ccte.api.rapidtox.chemical;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChemicalHeaderInfo {

    String preferredName;
    String dtxsid;
    String casrn;

}
