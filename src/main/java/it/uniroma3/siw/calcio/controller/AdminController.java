package it.uniroma3.siw.calcio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.calcio.model.Match;
import it.uniroma3.siw.calcio.model.MatchState;
import it.uniroma3.siw.calcio.model.Referee;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.MatchService;
import it.uniroma3.siw.calcio.service.RefereeService;
import it.uniroma3.siw.calcio.service.TeamService;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequestMapping("/admin") // to avoid the "/admin" repetition
public class AdminController {

    private final TournamentService tournamentService;
    private final MatchService matchService;
    private final TeamService teamService;
    private final RefereeService refereeService;

    public AdminController(TournamentService tournamentService, MatchService matchService, TeamService teamService, RefereeService refereeService) {
        this.tournamentService = tournamentService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.refereeService = refereeService;
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
        addMatchFormAttributes(model);
        return "admin/match/form";
    }

    @PostMapping("/matches")
    public String createMatch(@Valid @ModelAttribute("match") Match match, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addMatchFormAttributes(model);
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
        addMatchFormAttributes(model);
        return "admin/match/edit-form";
    }
    
    @PostMapping("/matches/{id}")
    public String updateMatch(@PathVariable Long id,
                              @Valid @ModelAttribute("match") Match formMatch,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long homeTeamId,
                              @RequestParam(required = false) Long awayTeamId,
                              @RequestParam(required = false) Long tournamentId,
                              @RequestParam(required = false) Long refereeId, // All optional so we can give back the errors without a 400 bad request
                              Model model) {
        formMatch.setId(id); // if the id's not set, when it has errors the url breaks
        Team homeTeam = findTeamById(homeTeamId);
        Team awayTeam = findTeamById(awayTeamId);
        Tournament tournament = findTournamentById(tournamentId);
        Referee referee = findRefereeById(refereeId);

        formMatch.setHomeTeam(homeTeam);
        formMatch.setAwayTeam(awayTeam);
        formMatch.setTournament(tournament);
        formMatch.setReferee(referee);

        if (homeTeam == null || awayTeam == null || tournament == null) {
            bindingResult.reject("match.requiredData");
        }
        if (homeTeam != null && awayTeam != null && homeTeam.getId().equals(awayTeam.getId())) {
            bindingResult.reject("match.sameTeams");
        }
        if (refereeId != null && referee == null) {
            bindingResult.reject("match.invalidReferee");
        }

        if (bindingResult.hasErrors()) {
            addMatchFormAttributes(model);
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
        return "redirect:/matches";
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

    private void addMatchFormAttributes(Model model) {
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("tournaments", tournamentService.findAll());
        model.addAttribute("referees", refereeService.findAll());
        model.addAttribute("matchStates", MatchState.values());
    }

    private Team findTeamById(Long id) {
        return id == null ? null : teamService.findById(id);
    }

    private Tournament findTournamentById(Long id) {
        return id == null ? null : tournamentService.findById(id);
    }

    private Referee findRefereeById(Long id) {
        return id == null ? null : refereeService.findById(id);
    }
}
