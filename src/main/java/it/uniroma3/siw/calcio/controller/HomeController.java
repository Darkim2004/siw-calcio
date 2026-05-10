package it.uniroma3.siw.calcio.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.TournamentService;


@Controller
public class HomeController {

    private static final int HOME_TOURNAMENTS_LIMIT = 5;
    private static final int HOME_MATCHES_LIMIT = 5;
    private static final DateTimeFormatter MATCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
    private static final DateTimeFormatter MATCH_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN);

    private final TournamentService tournamentService;
    private final MatchService matchService;

    public HomeController(TournamentService tournamentService, MatchService matchService) {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        List<Tournament> tournaments = this.tournamentService.findAll();
        boolean hasMoreTournaments = tournaments.size() > HOME_TOURNAMENTS_LIMIT;
        List<Match> matches = this.matchService.findFirstTodayMatches(HOME_MATCHES_LIMIT);

        model.addAttribute("tournaments", hasMoreTournaments ? tournaments.subList(0, HOME_TOURNAMENTS_LIMIT) : tournaments);
        model.addAttribute("hasMoreTournaments", hasMoreTournaments);
        model.addAttribute("matches", matches);
        model.addAttribute("matchDateFormatter", MATCH_DATE_FORMATTER);
        model.addAttribute("matchTimeFormatter", MATCH_TIME_FORMATTER);
        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    
    
}
