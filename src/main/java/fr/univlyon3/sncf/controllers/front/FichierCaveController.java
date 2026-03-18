package fr.univlyon3.sncf.controllers.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.univlyon3.sncf.repositories.FichierCaveRepository;
import fr.univlyon3.sncf.services.EnrichissementExportFichier;
import fr.univlyon3.sncf.services.FichierUploadService;
import jakarta.servlet.http.HttpSession;

@Controller("fichierCaveController")
public class FichierCaveController {

    private final FichierCaveRepository fichierCaveRepository;
    private final EnrichissementExportFichier enrichissementExportFichier;
    private final FichierUploadService fichierUploadService;

    public FichierCaveController(
            FichierCaveRepository fichierCaveRepository,
            EnrichissementExportFichier enrichissementExportFichier,
            FichierUploadService fichierUploadService
    ) {
        this.fichierCaveRepository = fichierCaveRepository;
        this.enrichissementExportFichier = enrichissementExportFichier;
        this.fichierUploadService = fichierUploadService;
    }

    @GetMapping("/fichiers")
    public String afficherFichiers(Model model, HttpSession session) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        model.addAttribute("fichiers", fichierCaveRepository.findAll());
        return "fichiers";
    }

    @PostMapping("/fichiers/upload-json")
    public String uploadJson(
            @RequestParam("jsonFile") MultipartFile jsonFile,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        try {
            fichierUploadService.sauvegarderJsonReferentiel(jsonFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Le fichier JSON a été ajouté dans le dossier référentiel.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur upload JSON : " + e.getMessage());
        }

        return "redirect:/fichiers";
    }

    @PostMapping("/fichiers/upload-xml")
    public String uploadXml(
            @RequestParam("xmlFile") MultipartFile xmlFile,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        try {
            fichierUploadService.sauvegarderXmlCave(xmlFile);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Le fichier XML a été ajouté dans le dossier d’entrée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur upload XML : " + e.getMessage());
        }

        return "redirect:/fichiers";
    }

    @PostMapping("/fichiers/enrichir")
    public String enrichirEtEnvoyer(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("gestionnaireConnecte") == null) {
            return "redirect:/login";
        }

        try {
            enrichissementExportFichier.enrichirEtArchiverFichiers();
            redirectAttributes.addFlashAttribute("successMessage",
                    "Le traitement a été lancé avec succès.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur pendant le traitement : " + e.getMessage());
        }

        return "redirect:/fichiers";
    }
}