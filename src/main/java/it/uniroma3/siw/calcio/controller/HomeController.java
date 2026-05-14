package it.uniroma3.siw.calcio.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.TeamService;
import it.uniroma3.siw.calcio.service.TournamentService;



@Controller
public class HomeController {

    private static final int HOME_TOURNAMENTS_LIMIT = 5;
    private static final int HOME_MATCHES_LIMIT = 5;
    private static final DateTimeFormatter MATCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
    private static final DateTimeFormatter MATCH_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN);
    private static final int HOME_TEAMS_LIMIT = 6;

    private final TournamentService tournamentService;
    private final MatchService matchService;
    private final TeamService teamService;

    public HomeController(TournamentService tournamentService, MatchService matchService, TeamService teamService) {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.teamService = teamService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        List<Tournament> tournaments = this.tournamentService.findFirstAlphabeticallyTournaments(HOME_TOURNAMENTS_LIMIT);
        List<Match> matches = this.matchService.findFirstTodayMatches(HOME_MATCHES_LIMIT);
        List<Team> teams = this.teamService.findFirstAlphabeticallyTeams(HOME_TEAMS_LIMIT);

        model.addAttribute("tournaments", tournaments);
        model.addAttribute("hasMoreTournaments", tournamentService.hasMoreTournaments(HOME_TOURNAMENTS_LIMIT));
        model.addAttribute("matches", matches);
        model.addAttribute("matchDateFormatter", MATCH_DATE_FORMATTER);
        model.addAttribute("matchTimeFormatter", MATCH_TIME_FORMATTER);
        model.addAttribute("teams", teams);
        model.addAttribute("hasMoreTeams", teamService.hasMoreTeams(HOME_TEAMS_LIMIT));
        return "index";
    }
}
