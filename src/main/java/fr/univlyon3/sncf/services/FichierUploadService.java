package fr.univlyon3.sncf.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("fichierUploadService")
public class FichierUploadService {

    @Value("${path.fichierReferentielGares}")
    private String dossierReferentiel;

    @Value("${path.dossierEntreesCAV}")
    private String dossierEntrees;

    public void sauvegarderJsonReferentiel(MultipartFile fichier) throws IOException {
        if (fichier.isEmpty()) {
            throw new IllegalArgumentException("Le fichier JSON est vide.");
        }

        String nom = fichier.getOriginalFilename();
        if (nom == null || !nom.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Le fichier doit être un JSON.");
        }

        Path cheminDossier = Path.of(dossierReferentiel);
        Files.createDirectories(cheminDossier);

        Path cheminFichier = cheminDossier.resolve(nom);
        Files.copy(fichier.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);
    }

    public void sauvegarderXmlCave(MultipartFile fichier) throws IOException {
        if (fichier.isEmpty()) {
            throw new IllegalArgumentException("Le fichier XML est vide.");
        }

        String nom = fichier.getOriginalFilename();
        if (nom == null || !nom.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("Le fichier doit être un XML.");
        }

        Path cheminDossier = Path.of(dossierEntrees);
        Files.createDirectories(cheminDossier);

        Path cheminFichier = cheminDossier.resolve(nom);
        Files.copy(fichier.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);
    }
}