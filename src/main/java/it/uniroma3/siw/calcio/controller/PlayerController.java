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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.RoleSoccer;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.service.PlayerService;
import it.uniroma3.siw.calcio.service.TeamService;
import jakarta.validation.Valid;

@Controller
public class PlayerController {

    private final PlayerService playerService;
    private final TeamService teamService;

    public PlayerController(PlayerService playerService, TeamService teamService) {
        this.playerService = playerService;
        this.teamService = teamService;
    }

    @GetMapping("/admin/players")
    public String getPlayers(Model model) {
        List<Player> players = playerService.findAllSortedByName();
        model.addAttribute("playersByRole", playerService.groupPlayersByRole(players));
        model.addAttribute("hasPlayers", !players.isEmpty());
        return "admin/player/list";
    }

    @GetMapping("/admin/players/new")
    public String getNewPlayerForm(Model model) {
        model.addAttribute("player", new Player());
        addGlobalPlayerFormAttributes(model, "/admin/players", "/admin/players", "New player", "Create player");
        return "admin/player/form";
    }

    @PostMapping("/admin/players")
    public String createPlayer(@Valid @ModelAttribute("player") Player player,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Long teamId,
                               Model model) {
        setPlayerTeam(player, teamId, bindingResult);

        if (bindingResult.hasErrors()) {
            addGlobalPlayerFormAttributes(model, "/admin/players", "/admin/players", "New player", "Create player");
            return "admin/player/form";
        }

        playerService.save(player);
        return "redirect:/admin/players";
    }

    @GetMapping("/admin/players/{id}/edit")
    public String getEditPlayerForm(@PathVariable Long id, Model model) {
        Player player = playerService.findById(id);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("player", player);
        addGlobalPlayerFormAttributes(model, "/admin/players/" + id, "/admin/players", "Edit player", "Save changes");
        return "admin/player/edit-form";
    }

    @PostMapping("/admin/players/{id}")
    public String updatePlayer(@PathVariable Long id,
                               @Valid @ModelAttribute("player") Player formPlayer,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Long teamId,
                               Model model) {
        formPlayer.setId(id);
        setPlayerTeam(formPlayer, teamId, bindingResult);

        if (bindingResult.hasErrors()) {
            addGlobalPlayerFormAttributes(model, "/admin/players/" + id, "/admin/players", "Edit player", "Save changes");
            return "admin/player/edit-form";
        }

        Player player = playerService.findById(id);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        copyPlayerFields(player, formPlayer);
        player.setTeam(formPlayer.getTeam());
        playerService.save(player);
        return "redirect:/admin/players";
    }

    @PostMapping("/admin/players/{id}/delete")
    public String deletePlayer(@PathVariable Long id) {
        Player player = playerService.findById(id);
        if (player == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        playerService.delete(player);
        return "redirect:/admin/players";
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

        player.setTeam(null);
        playerService.save(player);
        return "redirect:/admin/teams/" + teamId + "/edit";
    }

    @GetMapping("/admin/teams/{id}/players/new")
    public String getNewTeamPlayerForm(@PathVariable("id") Long teamId, Model model) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("player", new Player());
        addTeamPlayerFormAttributes(model, team, "/admin/teams/" + teamId + "/players",
                "/admin/teams/" + teamId + "/edit", "New player", "Create player");
        return "admin/player/form";
    }

    @PostMapping("/admin/teams/{id}/players")
    public String createTeamPlayer(@PathVariable("id") Long teamId,
                                   @Valid @ModelAttribute("player") Player player,
                                   BindingResult bindingResult,
                                   Model model) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (bindingResult.hasErrors()) {
            addTeamPlayerFormAttributes(model, team, "/admin/teams/" + teamId + "/players",
                    "/admin/teams/" + teamId + "/edit", "New player", "Create player");
            return "admin/player/form";
        }

        player.setTeam(team);
        playerService.save(player);
        return "redirect:/admin/teams/" + teamId + "/edit";
    }

    @GetMapping("/admin/teams/{teamId}/players/{playerId}/edit")
    public String getEditTeamPlayerForm(@PathVariable Long teamId, @PathVariable Long playerId, Model model) {
        Team team = teamService.findById(teamId);
        Player player = playerService.findById(playerId);
        validateTeamPlayer(team, player, teamId);

        model.addAttribute("player", player);
        addTeamPlayerFormAttributes(model, team, "/admin/teams/" + teamId + "/players/" + playerId,
                "/admin/teams/" + teamId + "/edit", "Edit player", "Save changes");
        return "admin/player/edit-form";
    }

    @PostMapping("/admin/teams/{teamId}/players/{playerId}")
    public String updateTeamPlayer(@PathVariable Long teamId,
                                   @PathVariable Long playerId,
                                   @Valid @ModelAttribute("player") Player formPlayer,
                                   BindingResult bindingResult,
                                   Model model) {
        Team team = teamService.findById(teamId);
        if (team == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        formPlayer.setId(playerId);
        if (bindingResult.hasErrors()) {
            addTeamPlayerFormAttributes(model, team, "/admin/teams/" + teamId + "/players/" + playerId,
                    "/admin/teams/" + teamId + "/edit", "Edit player", "Save changes");
            return "admin/player/edit-form";
        }

        Player player = playerService.findById(playerId);
        validateTeamPlayer(team, player, teamId);

        copyPlayerFields(player, formPlayer);
        playerService.save(player);
        return "redirect:/admin/teams/" + teamId + "/edit";
    }

    private void addGlobalPlayerFormAttributes(Model model, String formAction, String cancelUrl, String title,
                                               String submitLabel) {
        model.addAttribute("roles", RoleSoccer.values());
        model.addAttribute("teams", teamService.findAll());
        model.addAttribute("teamContext", false);
        model.addAttribute("formAction", formAction);
        model.addAttribute("cancelUrl", cancelUrl);
        model.addAttribute("formTitle", title);
        model.addAttribute("submitLabel", submitLabel);
    }

    private void addTeamPlayerFormAttributes(Model model, Team team, String formAction, String cancelUrl, String title,
                                             String submitLabel) {
        model.addAttribute("team", team);
        model.addAttribute("teamId", team.getId());
        model.addAttribute("roles", RoleSoccer.values());
        model.addAttribute("teamContext", true);
        model.addAttribute("formAction", formAction);
        model.addAttribute("cancelUrl", cancelUrl);
        model.addAttribute("formTitle", title);
        model.addAttribute("submitLabel", submitLabel);
    }

    private void setPlayerTeam(Player player, Long teamId, BindingResult bindingResult) {
        if (teamId == null) {
            player.setTeam(null);
            return;
        }

        Team team = teamService.findById(teamId);
        if (team == null) {
            bindingResult.reject("player.invalidTeam");
            player.setTeam(null);
            return;
        }

        player.setTeam(team);
    }

    private void validateTeamPlayer(Team team, Player player, Long teamId) {
        if (team == null || player == null || player.getTeam() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!player.getTeam().getId().equals(teamId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void copyPlayerFields(Player target, Player source) {
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setBirthDate(source.getBirthDate());
        target.setRole(source.getRole());
        target.setPhoto(source.getPhoto());
        target.setHeight(source.getHeight());
        target.setSquadNumber(source.getSquadNumber());
    }
}
