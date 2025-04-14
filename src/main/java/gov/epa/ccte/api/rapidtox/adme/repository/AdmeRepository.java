package gov.epa.ccte.api.rapidtox.adme.repository;

import gov.epa.ccte.api.rapidtox.adme.model.Adme;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmeRepository extends ReadOnlyRepository<Adme, Integer> {

    Adme findAdmeByDtxsidEquals(String dtxsid);
}
