package fr.univlyon3.sncf;

import fr.univlyon3.sncf.transverse.XMLWritter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLWritterTest {

    @Test
    public void testEnrichirXML() throws IOException {
        XMLWritter writter = new XMLWritter();
        String inputPath = "input/FichierCAVE_AQU_X12345_29062022.xml";
        String outputPath = "target/test-output/FichierCAVE_AQU_X12345_29062022_enriched.xml";
        String expectedResult = "target/FichierCAVE_AQU_X12345_29062022_enriched.xml";

        writter.enrichirXML(inputPath, outputPath);

        String actualContent = Files.readString(Paths.get(outputPath)).replace("\r\n", "\n");

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
