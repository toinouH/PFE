package fr.univlyon3.sncf.transverse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/// Cette classe tiendra la gestion des dossiers par région
/// On va créer les dossiers par défaut pour chaque région
@Component("gestionnaireDossiers")
public class GestionnaireDossiers {

    private static final Logger LOGGER = LogManager.getLogger(GestionnaireDossiers.class);

    private final Path path;

    // Uniquement pour les test
    public GestionnaireDossiers(String pathDossier) {
        this(Path.of(pathDossier));
    }

    @Autowired
    public GestionnaireDossiers(@Value("${path.dossierCAVEnrichisTries}") Path path) {
        this.path = path;
    }

    public void creerDossiersRegions() throws IOException {
        Files.createDirectories(path);

        for (Regions region : Regions.values()) {
            Path dossierRegion = path.resolve(region.name());
            if (Files.exists(dossierRegion)) {
                LOGGER.info("Dossier {} déjà existant", dossierRegion);
                continue;
            } else {
                LOGGER.info("Création du dossier {}", dossierRegion);
                Files.createDirectories(dossierRegion);
            }
        }
    }

    public Path getPath() {
        return path;
    }
}
