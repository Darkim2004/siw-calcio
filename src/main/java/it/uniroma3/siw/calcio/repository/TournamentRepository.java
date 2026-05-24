package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.calcio.model.Tournament;

public interface TournamentRepository extends CrudRepository<Tournament, Long> {

    // SQL query because JPQL dosent support limit
    @Query(value = """
            select *
            from tournament t
            order by lower(t.name), t.name
            limit :limit
            """, nativeQuery = true)
    public List<Tournament> findFirstAlphabeticallyTournaments(@Param("limit") int limit);
}
