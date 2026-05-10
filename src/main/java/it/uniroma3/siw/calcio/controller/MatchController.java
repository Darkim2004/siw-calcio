package it.uniroma3.siw.calcio.controller;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.siw.calcio.service.MatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
public class MatchController {

    private static final DateTimeFormatter MATCH_DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
    private static final DateTimeFormatter MATCH_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm", Locale.ITALIAN);

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/matches")
    public String getMatches(Model model) {
        model.addAttribute("matches", matchService.findAllSortedByDateTime());
        model.addAttribute("matchDateFormatter", MATCH_DATE_FORMATTER);
        model.addAttribute("matchTimeFormatter", MATCH_TIME_FORMATTER);
        return "match/list";
    }

    @GetMapping("/matches/{id}")
    public String getMatch(@PathVariable Long id, Model model) {
        model.addAttribute(matchService.findById(id));
        return "match/detail";
    }
    
    
}
