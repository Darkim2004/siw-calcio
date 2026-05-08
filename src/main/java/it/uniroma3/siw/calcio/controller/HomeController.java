package it.uniroma3.siw.calcio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.calcio.service.TournamentService;


@Controller
public class HomeController {

    private final TournamentService tournamentService;

    public HomeController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("tournaments", this.tournamentService.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    
    
}
