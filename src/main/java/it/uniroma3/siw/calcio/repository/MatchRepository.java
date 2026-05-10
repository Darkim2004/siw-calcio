package it.uniroma3.siw.calcio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;

public interface MatchRepository extends CrudRepository<Match, Long> {

    @Query("""
            select m
            from Match m
            where m.tournament = :tournament
            and (m.homeTeam = :team or m.awayTeam = :team)
            """)
    List<Match> findByTournamentAndTeam(@Param("tournament") Tournament tournament, @Param("team") Team team);

}
