package fr.univlyon3.sncf.transverse;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/// Cette classe tiendra la gestion des dossiers par région
/// On va créer les dossiers par défaut pour chaque région
public class GestionnaireDossiers {

    private Path path;

    public GestionnaireDossiers(String pathDossier) {
        this(Path.of(pathDossier));
    }

    // TODO Je ne suis pas certain de l'usage de cette variable de paramétrage; peut-être refacto pour fixer
    public GestionnaireDossiers(@Value("${path.dossierCAVEnrichisTries}") Path path) {
        this.path = path;
    }

    public void creerDossiersRegions() throws IOException {
        Files.createDirectories(path);

        for (Regions region : Regions.values()) {
            Path dossierRegion = path.resolve(region.name());
            Files.createDirectories(dossierRegion);
        }
    }

    public Path getPath() {
        return path;
    }
}
