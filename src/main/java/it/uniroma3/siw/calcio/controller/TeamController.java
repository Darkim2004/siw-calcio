package it.uniroma3.siw.calcio.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.RoleSoccer;
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
            Map<String, List<Player>> playersByRole = new LinkedHashMap<>();
            playersByRole.put("Forwards", filterPlayersByRole(players, RoleSoccer.FORWARD));
            playersByRole.put("Midfielders", filterPlayersByRole(players, RoleSoccer.MIDFIELDER));
            playersByRole.put("Defenders", filterPlayersByRole(players, RoleSoccer.DEFENDER));
            playersByRole.put("Goalkeepers", filterPlayersByRole(players, RoleSoccer.GOALKEEPER));
            model.addAttribute("playersByRole", playersByRole);
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

    @PostMapping("/admin/teams/{id}/players/add")
    public String addExistingPlayer(@PathVariable("id") Long teamId, @RequestParam Long playerId) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Player player = playerService.findById(playerId);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        player.setTeam(team);
        playerService.save(player);
        return "redirect:/admin/teams/" + teamId + "/edit";
    }

    @PostMapping("/admin/teams/{id}/players/remove")
    public String removePlayer(@PathVariable("id") Long teamId, @RequestParam Long playerId) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Player player = playerService.findById(playerId);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (player.getTeam() == null || !player.getTeam().getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        playerService.delete(player);
        return "redirect:/admin/teams/" + teamId + "/edit";
    }

    @GetMapping("/admin/teams/{id}/players/new")
    public String newPlayerForm(@PathVariable("id") Long teamId, Model model) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("player", new Player());
        addPlayerFormAttributes(model, team);
        return "admin/player/form";
    }

    @GetMapping("/admin/teams/{teamId}/players/{playerId}/edit")
    public String getEditPlayerForm(@PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        Team team = teamService.findById(teamId);
        Player player = playerService.findById(playerId);
        if (team == null || player == null || player.getTeam() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!player.getTeam().getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        model.addAttribute("player", player);
        model.addAttribute("team", team);
        model.addAttribute("roles", RoleSoccer.values());
        return "admin/player/edit-form";
    }

    @PostMapping("/admin/teams/{teamId}/players/{playerId}")
    public String updatePlayer(@Valid @ModelAttribute("player") Player formPlayer, BindingResult bindingResult, @PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        formPlayer.setId(playerId);
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (bindingResult.hasErrors()) {
            addPlayerFormAttributes(model, teamService.findById(teamId));
            return "admin/player/edit-form";
        }

        Player player = playerService.findById(playerId);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (player.getTeam() == null || !player.getTeam().getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        player.setFirstName(formPlayer.getFirstName());
        player.setLastName(formPlayer.getLastName());
        player.setBirthDate(formPlayer.getBirthDate());
        player.setRole(formPlayer.getRole());
        player.setPhoto(formPlayer.getPhoto());
        player.setHeight(formPlayer.getHeight());
        player.setSquadNumber(formPlayer.getSquadNumber());
        playerService.save(player);
        return "redirect:/admin/teams/" + teamId + "/edit";
    }
        

    @PostMapping("/admin/teams/{id}/players")
    public String createPlayer(@PathVariable("id") Long teamId,
                               @Valid @ModelAttribute("player") Player player,
                               BindingResult bindingResult,
                               Model model) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (bindingResult.hasErrors()) {
            addPlayerFormAttributes(model, team);
            return "admin/player/form";
        }

        player.setTeam(team);
        playerService.save(player);

        return "redirect:/admin/teams/" + teamId + "/edit";
    }

    private List<Player> filterPlayersByRole(List<Player> players, RoleSoccer role) {
        return players.stream()
                .filter(player -> role.equals(player.getRole()))
                .toList();
    }

    private void addTeamFormAttributes(Model model) {
        model.addAttribute("players", playerService.findAllPlayers());
    }

    private void addTeamEditAttributes(Model model, Team team) {
        List<Player> teamPlayers = teamService.findPlayersByTeamId(team.getId());
        List<Player> availablePlayers = playerService.findAllPlayers();

        model.addAttribute("teamPlayers", teamPlayers);
        model.addAttribute("players", availablePlayers);
    }

    private void addPlayerFormAttributes(Model model, Team team) {
        model.addAttribute("team", team);
        model.addAttribute("teamId", team.getId());
        model.addAttribute("roles", RoleSoccer.values());
    }
}
