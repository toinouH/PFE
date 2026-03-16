package fr.univlyon3.sncf;

import fr.univlyon3.sncf.transverse.XMLWritter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLWritterTest {

    @Test
    void doitEcrireLeFichierEnrichiDansLeDossierDeSortieAssortiDuSousDossierRegion() throws IOException {
        XMLWritter writter = new XMLWritter();
        String inputPath = "input/FichierCAVE_AQU_X12345_29062022.xml";
        String outputPath = "target/test-output/";
        String expectedResult = "target/AQU/FichierCAVE_AQU_X12345_29062022_enriched.xml";

        writter.enrichirXML(inputPath, outputPath);

        Path actualOutputFile = Paths.get(outputPath, "AQU", "FichierCAVE_AQU_X12345_29062022_enriched.xml");

        assertTrue(Files.exists(actualOutputFile), "Le fichier de sortie doit être créé dans le sous-dossier de région");
        assertTrue(Files.isRegularFile(actualOutputFile), "Le chemin de sortie doit pointer vers un fichier");

        String actualContent = Files.readString(actualOutputFile).replace("\r\n", "\n");

        String expectedContent;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(expectedResult)) {
            if (is == null) {
                throw new IOException("Fichier de référence introuvable : " + expectedResult);
            }
            expectedContent = new String(is.readAllBytes(), StandardCharsets.UTF_8).replace("\r\n", "\n");
        }

        assertEquals(expectedContent, actualContent, "Le fichier généré ne correspond pas au fichier de référence");
    }
}