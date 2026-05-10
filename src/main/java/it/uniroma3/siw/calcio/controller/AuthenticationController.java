package it.uniroma3.siw.calcio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    
    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

}
