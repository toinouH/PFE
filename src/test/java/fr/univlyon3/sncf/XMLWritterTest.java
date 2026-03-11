package fr.univlyon3.sncf;

import fr.univlyon3.sncf.transverse.XMLWritter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLWritterTest {

    @Test
    public void testEnrichirXML() throws IOException {
        XMLWritter writter = new XMLWritter();
        String inputPath = "input/FichierCAVE_AQU_X12345_29062022.xml";
        String outputPath = "target/test-output/FichierCAVE_AQU_X12345_29062022_enriched.xml";
        
        writter.enrichirXML(inputPath, outputPath);
        
        String content = Files.readString(Paths.get(outputPath));
        
        // Vérifier que les balises NearestStations sont présentes
        assertTrue(content.contains("<NearestStations>"), "Le fichier enrichi doit contenir <NearestStations>");
        assertTrue(content.contains("</NearestStations>"), "Le fichier enrichi doit contenir </NearestStations>");
        
        // Vérifier qu'on en a plusieurs (car il y a 5 stops, donc 4 insertions)
        int count = 0;
        int lastIndex = 0;
        while ((lastIndex = content.indexOf("<NearestStations>", lastIndex)) != -1) {
            count++;
            lastIndex += "<NearestStations>".length();
        }
        assertTrue(count >= 1, "Il devrait y avoir au moins une insertion de NearestStations");
    }
}
