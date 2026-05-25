package it.uniroma3.siw.calcio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.calcio.model.Referee;
import it.uniroma3.siw.calcio.service.RefereeService;
import jakarta.validation.Valid;

@Controller
public class RefereeController {

    private final RefereeService refereeService;

    public RefereeController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    @GetMapping("/admin/referees")
    public String getReferees(Model model) {
        model.addAttribute("referees", refereeService.findAllSortedByName());
        return "admin/referee/list";
    }

    @GetMapping("/admin/referees/new")
    public String getNewRefereeForm(Model model) {
        model.addAttribute("referee", new Referee());
        addRefereeFormAttributes(model, "/admin/referees", "New referee", "Create referee");
        return "admin/referee/form";
    }

    @PostMapping("/admin/referees")
    public String createReferee(@Valid @ModelAttribute("referee") Referee referee, BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            addRefereeFormAttributes(model, "/admin/referees", "New referee", "Create referee");
            return "admin/referee/form";
        }

        refereeService.save(referee);
        return "redirect:/admin/referees";
    }

    @GetMapping("/admin/referees/{id}/edit")
    public String getEditRefereeForm(@PathVariable Long id, Model model) {
        Referee referee = refereeService.findById(id);
        if (referee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("referee", referee);
        addRefereeFormAttributes(model, "/admin/referees/" + id, "Edit referee", "Save changes");
        return "admin/referee/edit-form";
    }

    @PostMapping("/admin/referees/{id}")
    public String updateReferee(@PathVariable Long id, @Valid @ModelAttribute("referee") Referee formReferee,
                                BindingResult bindingResult, Model model) {
        Referee referee = refereeService.findById(id);
        if (referee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        formReferee.setId(id);
        if (bindingResult.hasErrors()) {
            addRefereeFormAttributes(model, "/admin/referees/" + id, "Edit referee", "Save changes");
            return "admin/referee/edit-form";
        }

        referee.setFirstName(formReferee.getFirstName());
        referee.setLastName(formReferee.getLastName());
        referee.setRefereeCode(formReferee.getRefereeCode());
        refereeService.save(referee);
        return "redirect:/admin/referees";
    }

    @PostMapping("/admin/referees/{id}/delete")
    public String deleteReferee(@PathVariable Long id) {
        Referee referee = refereeService.findById(id);
        if (referee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        refereeService.delete(referee);
        return "redirect:/admin/referees";
    }

    private void addRefereeFormAttributes(Model model, String formAction, String title, String submitLabel) {
        model.addAttribute("formAction", formAction);
        model.addAttribute("formTitle", title);
        model.addAttribute("submitLabel", submitLabel);
    }
}
