package gov.epa.ccte.api.rapidtox.chemical.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

/**
 *
 * @author arashid
 * Create at 2021-04-13 13:24
 */
@Slf4j
@Entity
@Table(name = "recent_searches", schema = "rapidtox_app_data")
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class RecentSearch {

    /**
     *
     */
    @Id
    @Setter(AccessLevel.PROTECTED)
    @NonNull
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *
     */
    @Column(name = "user_name", length = 50)
    private String userName;

    /**
     *
     */
    @Column(name = "search_text")
    private String searchText;

    /**
     *
     */
    @Column(name = "searched_dt")
    private OffsetDateTime searchedOn;

    public RecentSearch(String userName, String searchText) {
        log.debug("user name={}, search text={}", userName, searchText);
        this.userName = userName;
        this.searchText = searchText;
        this.searchedOn = OffsetDateTime.now();
    }
}
