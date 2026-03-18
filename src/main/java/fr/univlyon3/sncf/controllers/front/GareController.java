package fr.univlyon3.sncf.controllers.front;

import fr.univlyon3.sncf.models.Gestionnaire;
import fr.univlyon3.sncf.services.GareService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("gareController")
public class GareController {

    private final GareService gareService;

    public GareController(@Qualifier("gareService") GareService gareService) {
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