package it.uniroma3.siw.calcio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.TournamentService;

@Controller
public class TournamentController {

    private TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService, MatchService matchService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/tournaments")
    public String getTournaments(Model model) {
        model.addAttribute("tournaments", tournamentService.findAll());
        return "tournament/list";
    }

    @GetMapping("/tournaments/form")
    public String getTournamentForm(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "tournament/form";
    }

    @GetMapping("/tournaments/{id}")
    public String getTournament(@PathVariable Long id, Model model) {
        model.addAttribute("tournament", tournamentService.findById(id));
        model.addAttribute("teamsWithPoints", tournamentService.findTeamsWithPointsByTournamentId(id));
        model.addAttribute("teamsWithLastPoints", tournamentService.findTeamsWithLastPointsByTournamentId(id));
        return "tournament/detail";
    }
}
