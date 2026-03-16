package fr.univlyon3.sncf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import fr.univlyon3.sncf.models.Gestionnaire;
import  fr.univlyon3.sncf.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {

     private final AuthService authService;

    public ViewController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login, @RequestParam String motPasse,Model model,
            HttpSession session) {
        Gestionnaire gestionnaire = authService.authenticate(login, motPasse);

        if (gestionnaire != null) {
            session.setAttribute("gestionnaireConnecte", gestionnaire);
            model.addAttribute("gestionnaire", session.getAttribute("gestionnaireConnecte"));
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Invalid login or password");
        return "login";
    }

    @GetMapping("/creer-compte")
    public String showCreateAccountPage() {
        return "creer-compte";
    }

    @PostMapping("/creer-compte")
    public String createAccount(@RequestParam String login, @RequestParam String nom,
            @RequestParam String prenom, @RequestParam String motPasse,@RequestParam String confirmMotPasse, Model model) {
        Gestionnaire gestionnaire = new Gestionnaire();
        gestionnaire.setLogin(login);
        gestionnaire.setNom(nom);
        gestionnaire.setPrenom(prenom);
        gestionnaire.setMotPasse(motPasse);

        if (!motPasse.equals(confirmMotPasse)) {
            model.addAttribute("error", "Passwords do not match");
            return "creer-compte";
        }

        authService.register(gestionnaire);

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        return "dashboard";
    }

    @GetMapping("/fichiers")
    public String fichiers(Model model) {
        return "fichiers";
    }

    

    @GetMapping("/frequentations")
    public String frequentations(Model model) {
        return "frequentations";
    }
}