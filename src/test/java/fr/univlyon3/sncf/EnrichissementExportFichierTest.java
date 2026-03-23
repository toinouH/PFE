package fr.univlyon3.sncf;

import fr.univlyon3.sncf.services.EnrichissementExportFichierImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "path.dossierEntreesCAV=/path/to/dossiers/entrees",
        "path.dossierCAVEnrichisTries=/path/to/dossiers/sorties",
        "path.fichierReferentielGares=/path/to/dossiers/referentiel"
})
public class EnrichissementExportFichierTest {

    @Autowired
    private EnrichissementExportFichierImpl service;

    @Disabled("Ceci est un quasiment un test de production et peut pointer vers un autre dossier système")
    @Test
    void lanceLeTraitement() throws Exception {
        service.enrichirEtArchiverFichiers(null);

        Path fichierGenere = Path.of("target/test-output", "AQU", "FichierCAVE_AQU_X12345_29062022_enriched.xml");

        assertTrue(Files.exists(fichierGenere), "Le fichier enrichi doit être généré");
        assertTrue(Files.isRegularFile(fichierGenere), "Le chemin généré doit pointer vers un fichier");
    }
}