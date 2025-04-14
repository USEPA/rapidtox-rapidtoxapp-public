package gov.epa.ccte.api.rapidtox.session.controller;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateSessionRequest {
    @NotNull
    private String activeTab;
    @NotNull
    private String tabName;
    @NotNull
    private String tabData;
    @NotNull
    private String sessionKey;
}
