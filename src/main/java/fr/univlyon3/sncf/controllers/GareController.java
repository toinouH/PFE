package fr.univlyon3.sncf.controllers;

import fr.univlyon3.sncf.services.GareService;
import fr.univlyon3.sncf.models.Gestionnaire;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GareController {

    private final GareService gareService;

    public GareController(GareService gareService) {
        this.gareService = gareService;
    }

    @GetMapping("/gares")
    public String afficherGares(Model model, HttpSession session) {
       Gestionnaire gestionnaireConnecte = (Gestionnaire) session.getAttribute("gestionnaireConnecte");

       if (gestionnaireConnecte == null) {
            return "redirect:/login";
        }
        model.addAttribute("gares", gareService.getAllGares());
        return "gares";
    }
}