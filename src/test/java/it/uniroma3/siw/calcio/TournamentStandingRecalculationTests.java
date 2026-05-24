package it.uniroma3.siw.calcio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.MatchState;
import it.uniroma3.siw.calcio.model.Partecipation;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.repository.PartecipationRepository;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.TeamService;
import it.uniroma3.siw.calcio.service.TournamentService;

@SpringBootTest
@Transactional
class TournamentStandingRecalculationTests {

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private PartecipationRepository partecipationRepository;

    @Test
    void recalculateStandingsRebuildsPointsFromPlayedMatches() {
        Tournament tournament = new Tournament();
        tournament.setName("Standing recalculation test");
        tournament.setYear(2026);
        tournament = tournamentService.save(tournament);

        Team homeTeam = team("Standing Home");
        Team awayTeam = team("Standing Away");
        teamService.save(homeTeam);
        teamService.save(awayTeam);

        tournamentService.addTeamToTournament(tournament.getId(), homeTeam);
        tournamentService.addTeamToTournament(tournament.getId(), awayTeam);

        Match homeWin = match(tournament, homeTeam, awayTeam, MatchState.PLAYED, 2, 0, 1);
        Match draw = match(tournament, homeTeam, awayTeam, MatchState.PLAYED, 1, 1, 2);
        Match scheduled = match(tournament, homeTeam, awayTeam, MatchState.SCHEDULED, 9, 0, 3);
        matchService.save(homeWin);
        matchService.save(draw);
        matchService.save(scheduled);

        tournamentService.recalculateStandings(tournament.getId());

        assertEquals(4, pointsOf(tournament, homeTeam));
        assertEquals(1, pointsOf(tournament, awayTeam));

        homeWin.setGoalsHome(0);
        homeWin.setGoalsAway(3);
        matchService.save(homeWin);

        tournamentService.recalculateStandings(tournament.getId());

        assertEquals(1, pointsOf(tournament, homeTeam));
        assertEquals(4, pointsOf(tournament, awayTeam));
    }

    private Team team(String name) {
        Team team = new Team();
        team.setName(name);
        team.setCity("Roma");
        team.setFoundationYear(2026);
        return team;
    }

    private Match match(Tournament tournament, Team homeTeam, Team awayTeam, MatchState state, int homeGoals,
            int awayGoals, int daysFromNow) {
        Match match = new Match();
        match.setTournament(tournament);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setState(state);
        match.setGoalsHome(homeGoals);
        match.setGoalsAway(awayGoals);
        match.setDateTime(LocalDateTime.now().plusDays(daysFromNow));
        match.setVenue("Test venue");
        return match;
    }

    private int pointsOf(Tournament tournament, Team team) {
        Partecipation partecipation = partecipationRepository.findByTournament_IdAndTeam_Id(
                tournament.getId(), team.getId());
        return partecipation.getPoints();
    }
}
