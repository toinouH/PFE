package fr.univlyon3.sncf.controllers;

import fr.univlyon3.sncf.repositories.FichierCaveRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FichierCaveController {

    private final FichierCaveRepository fichierCaveRepository;

    public FichierCaveController(FichierCaveRepository fichierCaveRepository) {
        this.fichierCaveRepository = fichierCaveRepository;
    }

    @GetMapping("/fichiers")
    public String afficherFichiers(Model model, HttpSession session) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        model.addAttribute("fichiers", fichierCaveRepository.findAll());
        return "fichiers";
    }
}