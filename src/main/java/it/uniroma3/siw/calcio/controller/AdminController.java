package it.uniroma3.siw.calcio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;



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

    @GetMapping("/tournaments/{id}/edit")
    public String getTournamentForm(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.findById(id);
        if (tournament == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("tournament", tournament);
        return "admin/tournaments/edit-form";
    }

    @PostMapping("/tournaments/{id}")
    public String updateTournament(@PathVariable Long id, @Valid @ModelAttribute("tournament") Tournament formTournament, BindingResult bindingResult) {
        formTournament.setId(id); // if the id's not set, when it has errors the url breaks
        if (bindingResult.hasErrors()) {
            return "admin/tournaments/edit-form";
        }

        Tournament tournament = tournamentService.findById(id);
        if (tournament == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        tournament.setName(formTournament.getName());
        tournament.setYear(formTournament.getYear());
        
        tournamentService.save(tournament);
        return "redirect:/tournaments";
    }
}