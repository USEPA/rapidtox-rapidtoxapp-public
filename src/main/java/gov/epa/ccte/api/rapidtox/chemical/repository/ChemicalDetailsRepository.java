package gov.epa.ccte.api.rapidtox.chemical.repository;

import gov.epa.ccte.api.rapidtox.chemical.model.ChemicalDetails;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChemicalDetailsRepository extends ReadOnlyRepository<ChemicalDetails, Long> {
	ChemicalDetails findByDtxsid(String dtxsid);
}
