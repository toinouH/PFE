package fr.univlyon3.sncf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/fichiers")
    public String fichiers(Model model) {
        return "fichiers";
    }

    @GetMapping("/gares")
    public String gares(Model model) {
        return "gares";
    }

    @GetMapping("/frequentations")
    public String frequentations(Model model) {
        return "frequentations";
    }
}