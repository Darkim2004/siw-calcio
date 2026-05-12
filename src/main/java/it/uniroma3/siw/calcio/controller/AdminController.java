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

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/admin") // to avoid the "/admin" repetition
public class AdminController {

    private final TournamentService tournamentService;
    private final MatchService matchService;

    public AdminController(TournamentService tournamentService, MatchService matchService) {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
    }

    // ------------------ TOURNAMENTS -------------------

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
        tournament.setDescription(formTournament.getDescription());
        
        tournamentService.save(tournament);
        return "redirect:/tournaments/" + id;
    }

    @PostMapping("/tournaments/{id}/delete")
    public String deleteTournament(@PathVariable Long id) {
        Tournament tournament = tournamentService.findById(id);
        if (tournament == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        tournamentService.delete(tournament);
        return "redirect:/tournaments";
    }

    // ------------------ MATCHES -------------------

    @GetMapping("/matches/new")
    public String getMatchForm(Model model) {
        Match match = new Match();
        model.addAttribute("match", match);
        return "admin/match/form";
    }

    @PostMapping("/matches")
    public String createMatch(@Valid @ModelAttribute("match") Match match, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/match/form";
        }

        matchService.save(match);
        return "redirect:/matches";
    }

    @GetMapping("/matches/{id}/edit")
    public String getMatchForm(@PathVariable Long id, Model model) {
        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("match", match);
        return "admin/match/edit-form";
    }
    
    @PostMapping("/matches/{id}")
    public String updateMatch(@PathVariable Long id, @Valid @ModelAttribute("match") Match formMatch, BindingResult bindingResult) {
        formMatch.setId(id); // if the id's not set, when it has errors the url breaks
        if (bindingResult.hasErrors()) {
            return "admin/match/edit-form";
        }

        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        match.setHomeTeam(formMatch.getHomeTeam());
        match.setAwayTeam(formMatch.getAwayTeam());
        match.setDateTime(formMatch.getDateTime());
        match.setVenue(formMatch.getVenue());
        match.setState(formMatch.getState());
        match.setTournament(formMatch.getTournament());
        match.setGoalsHome(formMatch.getGoalsHome());
        match.setGoalsAway(formMatch.getGoalsAway());
        match.setReferee(formMatch.getReferee());
        
        matchService.save(match);
        return "redirect:/matches/" + id;
    }

    @PostMapping("/matches/{id}/delete")
    public String deleteMatch(@PathVariable Long id) {
        Match match = matchService.findById(id);
        if (match == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        matchService.delete(match);
        return "redirect:/matches";
    }
}