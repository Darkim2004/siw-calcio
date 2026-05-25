package it.uniroma3.siw.calcio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.service.PlayerService;
import it.uniroma3.siw.calcio.service.TeamService;
import jakarta.validation.Valid;


@Controller
public class TeamController {

    private final TeamService teamService;
    private final PlayerService playerService;

    public TeamController(TeamService teamService, PlayerService playerService) {
        this.teamService = teamService;
        this.playerService = playerService;
    }

    @GetMapping("/teams/{id}")
    public String getTeam(@PathVariable Long id, Model model) {
        Team team = this.teamService.findById(id);
        model.addAttribute("team", team);

        if (team != null) {
            List<Player> players = this.teamService.findPlayersByTeamId(id);
            model.addAttribute("playersByRole", playerService.groupPlayersByRole(players));
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found");
        }

        return "team/detail";
    }

    @GetMapping("/teams")
    public String getTeams(Model model) {
        List<Team> teams = this.teamService.findAll();
        model.addAttribute("teams", teams);
        return "team/list";
    }

    @GetMapping("/admin/teams/new")
    public String getNewTeamForm(Model model) {
        model.addAttribute("team", new Team());
        addTeamFormAttributes(model);
        return "admin/team/form";
    }

    @PostMapping("/admin/teams")
    public String createTeam(@Valid @ModelAttribute("team") Team team, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            addTeamFormAttributes(model);
            return "admin/team/form";
        }

        teamService.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/admin/teams/{id}/edit")
    public String getEditTeamForm(@PathVariable Long id, Model model) {
        Team team = teamService.findById(id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("team", team);
        addTeamEditAttributes(model, team);
        return "admin/team/edit-form";
    }

    @PostMapping("/admin/teams/{id}")
    public String updateTeam(@PathVariable Long id, @Valid @ModelAttribute("team") Team formTeam, BindingResult bindingResult, Model model) {
        formTeam.setId(id);
        if (bindingResult.hasErrors()) {
            addTeamEditAttributes(model, formTeam);
            return "admin/team/edit-form";
        }

        Team team = teamService.findById(id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        team.setName(formTeam.getName());
        team.setFoundationYear(formTeam.getFoundationYear());
        team.setCity(formTeam.getCity());
        team.setLogo(formTeam.getLogo());

        teamService.save(team);
        return "redirect:/teams";
    }

    @PostMapping("/admin/teams/{id}/delete")
    public String deleteTeam(@PathVariable Long id) {
        Team team = teamService.findById(id);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        teamService.delete(id);
        return "redirect:/teams";
    }

    private void addTeamFormAttributes(Model model) {
        model.addAttribute("players", playerService.findAllSortedByName());
    }

    private void addTeamEditAttributes(Model model, Team team) {
        List<Player> teamPlayers = teamService.findPlayersByTeamId(team.getId());
        List<Player> availablePlayers = playerService.findAllSortedByName();

        model.addAttribute("teamPlayers", teamPlayers);
        model.addAttribute("players", availablePlayers);
    }
}
