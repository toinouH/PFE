package fr.univlyon3.sncf.controllers;

import fr.univlyon3.sncf.repositories.GareRepository;
import fr.univlyon3.sncf.services.ReferentielGareService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GareController {

    private final GareRepository gareRepository;
    private final ReferentielGareService referentielGareService;

    public GareController(
            GareRepository gareRepository,
            ReferentielGareService referentielGareService
    ) {
        this.gareRepository = gareRepository;
        this.referentielGareService = referentielGareService;
    }

    @GetMapping("/gares")
    public String afficherGares(Model model, HttpSession session) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        model.addAttribute("gares", gareRepository.findAll());
        return "gares";
    }

    @PostMapping("/gares/upload-json")
    public String uploadJsonReferentiel(
            @RequestParam("jsonFile") MultipartFile jsonFile,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        try {
            ReferentielGareService.ImportResult result =
                    referentielGareService.sauvegarderEtImporterJson(jsonFile);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Référentiel importé : " + result.ajoutees()
                            + " gare(s) ajoutée(s), "
                            + result.ignorees() + " ignorée(s), sur "
                            + result.total() + " ligne(s)."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Erreur lors de l'import du référentiel : " + e.getMessage()
            );
        }

        return "redirect:/gares";
    }
}