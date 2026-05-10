package it.uniroma3.siw.calcio.controller;

import org.springframework.validation.BindingResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.calcio.model.User;
import it.uniroma3.siw.calcio.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    
    @GetMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               HttpServletRequest request) throws ServletException {
        if (user.getUsername() != null
                && !user.getUsername().isBlank()
                && this.userService.findByUsername(user.getUsername()) != null) {
            bindingResult.rejectValue("username", "duplicate", "Username gia in uso");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        String rawPassword = user.getPassword();
        this.userService.save(user);
        request.login(user.getUsername(), rawPassword);
        return "redirect:/";
    }
}
