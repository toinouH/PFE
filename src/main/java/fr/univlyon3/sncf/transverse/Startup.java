package fr.univlyon3.sncf.transverse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/// Cette classe vérifie au démarrage de l'application que les dossiers nécessaires existent et les crée si nécessaire.
/// Si ce n'est pas le cas alors on le crée
@Component("startup")
public class Startup implements CommandLineRunner {

    private static final Logger LOGGER = LogManager.getLogger(Startup.class);

    private final Path dossierEntreesCAV;
    private final Path dossierCAVEnrichisTries;
    private final Path fichierReferentielGares;

    public Startup(
            @Value("${path.dossierEntreesCAV}") Path dossierEntreesCAV,
            @Value("${path.dossierCAVEnrichisTries}") Path dossierCAVEnrichisTries,
            @Value("${path.fichierReferentielGares}") Path fichierReferentielGares) {
        this.dossierEntreesCAV = dossierEntreesCAV;
        this.dossierCAVEnrichisTries = dossierCAVEnrichisTries;
        this.fichierReferentielGares = fichierReferentielGares;
    }

    @Override
    public void run(String... args) throws Exception {
        verifierEtCreerDossier(dossierEntreesCAV);
        verifierEtCreerDossier(dossierCAVEnrichisTries);
        verifierEtCreerDossier(fichierReferentielGares);

        // Création des sous-dossiers par région pour le dossier de sortie
        GestionnaireDossiers gestionnaire = new GestionnaireDossiers(dossierCAVEnrichisTries);
        gestionnaire.creerDossiersRegions();
    }

    private void verifierEtCreerDossier(Path pathStr) throws IOException {
        if (pathStr != null && !pathStr.toString().isEmpty()) {
            Path path = pathStr.toAbsolutePath().normalize();
            if (Files.notExists(path)) {
                Files.createDirectories(path);
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("Dossier créé : " + path.toAbsolutePath());
                }
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Le dossier existe déjà : " + path.toAbsolutePath());
                }
            }
        } else {
            LOGGER.error("Le chemin du dossier est vide");
        }
    }
}
