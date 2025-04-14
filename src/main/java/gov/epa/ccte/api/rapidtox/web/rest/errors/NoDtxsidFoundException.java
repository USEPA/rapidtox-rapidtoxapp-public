package gov.epa.ccte.api.rapidtox.web.rest.errors;

public class NoDtxsidFoundException extends RuntimeException  {
    @java.io.Serial
    static final long serialVersionUID = 1L;
    
    public NoDtxsidFoundException(String dtxsid,String sessionKey) {
        super("No matching records found for dtxsid " + dtxsid +" or sessionkey  " +sessionKey);
    }
}
