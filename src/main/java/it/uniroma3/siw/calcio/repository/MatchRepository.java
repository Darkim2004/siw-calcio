package it.uniroma3.siw.calcio.repository;

import java.time.LocalDateTime;
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

    List<Match> findByTournament_Id(Long tournamentId);

    List<Match> findByHomeTeam_IdOrAwayTeam_Id(Long id, Long id2);

    @Query("""
        SELECT m
        FROM Match m
        WHERE m.dateTime IS NOT NULL
        ORDER BY m.dateTime
        """)
    List<Match> findAllSortedByDateTime();

    @Query("""
        SELECT m
        FROM Match m
        WHERE m.dateTime IS NOT NULL
        AND m.dateTime >= :inizioGiorno
        AND m.dateTime < :fineGiorno
        ORDER BY m.dateTime
        """)
    List<Match> findTodayMatches(@Param("inizioGiorno") LocalDateTime inizioGiorno, @Param("fineGiorno") LocalDateTime fineGiorno);

    @Query(value = """
        SELECT *
        FROM match m
        WHERE m.date_time IS NOT NULL
        AND m.date_time >= :inizioGiorno
        AND m.date_time < :fineGiorno
        ORDER BY m.date_time
        LIMIT :limit
        """, nativeQuery = true)
    List<Match> findFirstTodayMatches(@Param("limit") int limit, @Param("inizioGiorno") LocalDateTime inizioGiorno, @Param("fineGiorno") LocalDateTime fineGiorno);

}
