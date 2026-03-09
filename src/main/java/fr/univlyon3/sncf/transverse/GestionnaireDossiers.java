package fr.univlyon3.sncf.transverse;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import fr.univlyon3.sncf.transverse.Regions;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/// Cette classe tiendra la gestion des dossiers par région
/// On va créer les dossiers par défaut pour chaque région
public class GestionnaireDossiers {

    private final Path path /*= Path.of("dossiers")*/;

    public GestionnaireDossiers(String pathDossier) {
        this(Path.of(pathDossier));
    }

    public GestionnaireDossiers(Path path) {
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
