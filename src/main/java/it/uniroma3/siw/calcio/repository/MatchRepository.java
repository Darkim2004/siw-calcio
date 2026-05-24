package it.uniroma3.siw.calcio.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.MatchState;

public interface MatchRepository extends CrudRepository<Match, Long> {

    List<Match> findByTournament_Id(Long tournamentId);

    @EntityGraph(attributePaths = { "homeTeam", "awayTeam" })
    @Query("""
        SELECT m
        FROM Match m
        WHERE m.tournament.id = :tournamentId
        AND m.state = :state
        AND m.dateTime IS NOT NULL
        ORDER BY m.dateTime DESC
        """)
    List<Match> findByTournamentIdAndStateWithTeamsOrderByDateTimeDesc(
            @Param("tournamentId") Long tournamentId,
            @Param("state") MatchState state);

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
