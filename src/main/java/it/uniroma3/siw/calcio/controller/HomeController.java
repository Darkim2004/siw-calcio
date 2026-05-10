package it.uniroma3.siw.calcio.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.TournamentService;


@Controller
public class HomeController {

    private static final int HOME_TOURNAMENTS_LIMIT = 5;

    private final TournamentService tournamentService;

    public HomeController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        List<Tournament> tournaments = this.tournamentService.findAll();
        boolean hasMoreTournaments = tournaments.size() > HOME_TOURNAMENTS_LIMIT;

        model.addAttribute("tournaments",
                hasMoreTournaments ? tournaments.subList(0, HOME_TOURNAMENTS_LIMIT) : tournaments);
        model.addAttribute("hasMoreTournaments", hasMoreTournaments);
        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    
    
}
