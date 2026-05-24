package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.calcio.model.Team;

public interface TeamRepository extends CrudRepository<Team, Long> {
    @Query(value = """
            select *
            from team t
            order by lower(t.name), t.name
            limit :limit
            """, nativeQuery = true)
    public List<Team> findFirstAlphabeticallyTeams(@Param("limit") int limit);

}
