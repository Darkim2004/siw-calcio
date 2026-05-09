package it.uniroma3.siw.calcio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.calcio.service.TeamService;


@Controller
public class TeamController {
    TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/teams/{id}")
    public String getTeam(@PathVariable Long id, Model model) {
        model.addAttribute("team", this.teamService.findById(id));
        return "team/detail";
    }
    
}
