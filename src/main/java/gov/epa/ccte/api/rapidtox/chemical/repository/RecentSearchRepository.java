package gov.epa.ccte.api.rapidtox.chemical.repository;

import gov.epa.ccte.api.rapidtox.chemical.model.RecentSearch;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearch, Integer> {

    List<RecentSearch> findByUserNameEqualsOrderBySearchedOnDesc(String userName);
}
