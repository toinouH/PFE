package fr.univlyon3.sncf;

import fr.univlyon3.sncf.transverse.GestionnaireDossiers;
import fr.univlyon3.sncf.transverse.Regions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GestionnaireDossiersTest {

    @TempDir
    private static Path tempDir;

    @Disabled("Ne marche que sur Windows; pas trop d'intérêt de l'adapter sur Mac/Linux pour l'instant")
    @Test
    void doitCreerLesDossiersDesRegionsSilsNexistentPas() throws IOException {
        Path racine = tempDir.resolve("C:\\_DVT\\SNCF\\target\\Test");
        GestionnaireDossiers gestionnaireDossiers = new GestionnaireDossiers(racine);

        assertTrue(Files.notExists(racine));

        gestionnaireDossiers.creerDossiersRegions();

        assertTrue(Files.exists(racine));
        assertTrue(Files.isDirectory(racine));

        for (Regions region : Regions.values()) {
            Path dossierRegion = racine.resolve(region.name());
            assertTrue(Files.exists(dossierRegion));
            assertTrue(Files.isDirectory(dossierRegion));
        }
    }

    void supprimerDossier() throws IOException {

        tempDir.resolve("C:\\_DVT\\SNCF\\target\\Test");
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}
