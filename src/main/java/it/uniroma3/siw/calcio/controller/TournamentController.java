package it.uniroma3.siw.calcio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;

@Controller
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/tournaments")
    public String getTournaments(Model model) {
        model.addAttribute("tournaments", tournamentService.findAll());
        return "tournament/list";
    }

    @GetMapping("/tournaments/{id}")
    public String getTournament(@PathVariable Long id, Model model) {
        model.addAttribute("tournament", tournamentService.findById(id));
        model.addAttribute("teamsWithPoints", tournamentService.findTeamsWithPointsByTournamentId(id));
        model.addAttribute("teamsWithLastPoints", tournamentService.findTeamsWithLastPointsByTournamentId(id));
        return "tournament/detail";
    }

    @GetMapping("/admin/tournaments/new")
    public String getNewTournamentForm(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "admin/tournaments/form";
    }

    @PostMapping("/admin/tournaments")
    public String createTournament(@Valid @ModelAttribute("tournament") Tournament tournament, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/tournaments/form";
        }

        tournamentService.save(tournament);
        return "redirect:/tournaments";
    }

    @GetMapping("/admin/tournaments/{id}/edit")
    public String getEditTournamentForm(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentService.findById(id);
        if (tournament == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("tournament", tournament);
        return "admin/tournaments/edit-form";
    }

    @PostMapping("/admin/tournaments/{id}")
    public String updateTournament(@PathVariable Long id, @Valid @ModelAttribute("tournament") Tournament formTournament, BindingResult bindingResult) {
        formTournament.setId(id);
        if (bindingResult.hasErrors()) {
            return "admin/tournaments/edit-form";
        }

        Tournament tournament = tournamentService.findById(id);
        if (tournament == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        tournament.setName(formTournament.getName());
        tournament.setYear(formTournament.getYear());
        tournament.setDescription(formTournament.getDescription());

        tournamentService.save(tournament);
        return "redirect:/tournaments/" + id;
    }

    @PostMapping("/admin/tournaments/{id}/delete")
    public String deleteTournament(@PathVariable Long id) {
        Tournament tournament = tournamentService.findById(id);
        if (tournament == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        tournamentService.delete(tournament);
        return "redirect:/tournaments";
    }
}
