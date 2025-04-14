package gov.epa.ccte.api.rapidtox.bioactivity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clowder_plot_info", schema = "rapidtox")
@Data
@NoArgsConstructor
public class ClowderPlotInfo {
    
    @JsonProperty("dtxsid")
    @Column(name = "dtxsid")
    private String dtxsid;

    @Id
    @JsonProperty("clowderFileUid")
    @Column(name = "clowder_file_uid")
    private String clowderFileUid;

    @JsonProperty("downloadUrl")
    @Column(name = "download_url")
    private String downloadUrl;

    @JsonProperty("clowderFilename")
    @Column(name = "clowder_filename")
    private String clowderFilename;

    @JsonProperty("clowderFileCreatedBy")
    @Column(name = "clowder_file_created_str")
    private String clowderFileCreatedBy;

    @JsonProperty("sourceSystem")
    @Column(name = "source_system")
    private String sourceSystem;

}
