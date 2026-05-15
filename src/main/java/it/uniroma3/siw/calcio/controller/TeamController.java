package it.uniroma3.siw.calcio.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.calcio.model.Player;
import it.uniroma3.siw.calcio.model.RoleSoccer;
import it.uniroma3.siw.calcio.model.Team;
import it.uniroma3.siw.calcio.service.TeamService;



@Controller
public class TeamController {
    TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
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

        return "team/detail";
    }

    @GetMapping("/teams")
    public String getTeams(Model model) {
         List<Team> teams = this.teamService.findAll();
         model.addAttribute("teams", teams);
        return "team/list";
    }
    

    private List<Player> filterPlayersByRole(List<Player> players, RoleSoccer role) {
        return players.stream()
                .filter(player -> role.equals(player.getRole()))
                .toList();
    }
    
}
