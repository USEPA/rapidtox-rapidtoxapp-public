package gov.epa.ccte.api.rapidtox.chemical.repository;

import gov.epa.ccte.api.rapidtox.chemical.model.ChemicalSearch;
import gov.epa.ccte.api.rapidtox.repository.ReadOnlyRepository;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChemicalSearchRepository extends ReadOnlyRepository<ChemicalSearch, Integer> {

    List<ChemicalSearch> findTop20ByModifiedValueStartsWithOrderByRankAscSearchWordAsc(String word);

    ChemicalSearch findFirstByDtxsid(String dtxsid);

    @Query(value = "select id, dtxsid,\n"
            + "       case\n"
            + "           when lag(modified_value) over (partition by modified_value) is not null OR\n"
            + "                lead(modified_value) over (partition by modified_value) is not null\n"
            + "               then search_group || ' - WARNING: Synonym mapped to two or more chemicals'\n"
            + "           else search_group end as search_group,\n"
            + "       preferred_name, search_name, search_value, modified_value, dtxcid, rank, has_structure_image, casrn\n"
            + "from (\n"
            + "         select row_number() over (partition by modified_value, dtxsid order by rank asc) as rnk,\n"
            + "                id, search_name, search_group, preferred_name, search_value,modified_value,rank,dtxsid,dtxcid,has_structure_image, casrn\n"
            + "         from rapidtox.search_chemical c\n"
            + "         where c.modified_value in (:searchWords)\n"
            + "         group by id, search_name, search_group, preferred_name, search_value, modified_value, rank, dtxsid, dtxcid, has_structure_image, casrn\n"
            + "         order by modified_value\n"
            + "     ) x\n"
            + "where rnk = 1", nativeQuery = true)
    List<ChemicalSearch> getIdentifierResult(@Param("searchWords") Collection<String> searchWords);

}
