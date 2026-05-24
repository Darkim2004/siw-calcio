package it.uniroma3.siw.calcio.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Partecipation;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.model.Tournament;
import it.uniroma3.siw.calcio.service.TeamService;
import it.uniroma3.siw.calcio.service.TournamentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class TournamentController {

    private final TournamentService tournamentService;
    private final TeamService teamService;

    public TournamentController(TournamentService tournamentService, TeamService teamService) {
        this.tournamentService = tournamentService;
        this.teamService = teamService;
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

        populateTournamentEditorModel(id, tournament, model);
        return "admin/tournaments/edit-form";
    }

    @PostMapping("/admin/tournaments/{id}")
    public String updateTournament(@PathVariable Long id, @Valid @ModelAttribute("tournament") Tournament formTournament, BindingResult bindingResult, Model model) {
        formTournament.setId(id);
        if (bindingResult.hasErrors()) {
            populateTournamentEditorModel(id, formTournament, model);
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

    @PostMapping("/admin/tournaments/{id}/team/add")
    public String addTeamToTournament(@PathVariable Long id, @RequestParam Long teamId) {
        Tournament tournament = tournamentService.findById(id);
        Team team = teamService.findById(teamId);
        if (tournament == null || team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        tournamentService.addTeamToTournament(id, team);
        return "redirect:/admin/tournaments/" + id + "/edit";
    }

    @PostMapping("/admin/tournaments/{id}/team/delete")
    public String deleteTeamFromTournament(@PathVariable Long id, @RequestParam Long teamId) {
        Tournament tournament = tournamentService.findById(id);
        Team team = teamService.findById(teamId);
        if (tournament == null || team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        tournamentService.deleteTeamFromTournament(id, team);
        return "redirect:/admin/tournaments/" + id + "/edit";
    }

    private void populateTournamentEditorModel(Long tournamentId, Tournament tournament, Model model) {
        List<Partecipation> partecipations = tournamentService.findPartecipationsByTournamentId(tournamentId);
        Set<Long> registeredTeamIds = new HashSet<>();
        for (Partecipation partecipation : partecipations) {
            Team registeredTeam = partecipation.getTeam();
            if (registeredTeam != null && registeredTeam.getId() != null) {
                registeredTeamIds.add(registeredTeam.getId());
            }
        }

        List<Team> availableTeams = teamService.findAll().stream()
                .filter(team -> team.getId() != null && !registeredTeamIds.contains(team.getId()))
                .toList();

        model.addAttribute("tournament", tournament);
        model.addAttribute("teamParticipations", partecipations);
        model.addAttribute("availableTeams", availableTeams);
    }
}
