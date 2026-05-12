package it.uniroma3.siw.calcio.controller;

import java.time.Year;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/admin") // to avoid the "/admin" repetition
public class AdminController {

    private final TournamentService tournamentService;

    public AdminController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/tournaments/new")
    public String getTournamentForm(Model model) {
        Tournament tournament = new Tournament();
        model.addAttribute("tournament", tournament);
        return "admin/tournaments/form";
    }

    @PostMapping("/tournaments")
    public String createTournament(@Valid @ModelAttribute("tournament") Tournament tournament, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/tournaments/form";
        }

        tournamentService.save(tournament);
        return "redirect:/tournaments";
    }

}
