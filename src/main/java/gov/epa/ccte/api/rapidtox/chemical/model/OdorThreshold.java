package gov.epa.ccte.api.rapidtox.chemical.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "odor_thresholds", schema = "rapidtox")
@Data
public class OdorThreshold {

	@Id
	String dtxsid;
	String odor;
	@Column(name = "odor_threshold")
	String odorThreshold;
}
