package fr.univlyon3.sncf.controllers;

import fr.univlyon3.sncf.repositories.FrequentationGareRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrequentationGareController {

    private final FrequentationGareRepository frequentationGareRepository;

    public FrequentationGareController(FrequentationGareRepository frequentationGareRepository) {
        this.frequentationGareRepository = frequentationGareRepository;
    }

    @GetMapping("/frequentations")
    public String afficherFrequentations(Model model, HttpSession session) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        model.addAttribute("frequentations", frequentationGareRepository.findAll());
        return "frequentations";
    }
}