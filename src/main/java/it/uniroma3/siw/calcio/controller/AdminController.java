package it.uniroma3.siw.calcio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.calcio.model.Tournament;


@Controller
public class AdminController {

    @GetMapping("/admin/tournaments/new")
    public String getTournamentForm(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "admin/tournaments/form";
    }
    

}
